package app.web;

import app.post.model.Post;
import app.post.service.PostService;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.CreateNewPost;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getPostPage(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ModelAndView("redirect:/login");
        }

        List<Post> allSharedPosts = postService.getAllVisiblePosts();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("post");
        modelAndView.addObject("user", user);
        modelAndView.addObject("allSharedPosts", allSharedPosts);

        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView getNewPostPage(HttpSession session) {

        User user = (User) session.getAttribute("user");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-post");
        modelAndView.addObject("user", user);
        modelAndView.addObject("createNewPost", new CreateNewPost());

        return modelAndView;
    }

    @PostMapping("/add")
    public String createNewPost(@Valid CreateNewPost createNewPost, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "add-post";
        }

        User userFromSession = (User) session.getAttribute("user");
        User managedUser = userService.getById(userFromSession.getId());
        postService.create(createNewPost, managedUser);
        managedUser.setPosts(postService.getAllPostsForUser(managedUser));
        session.setAttribute("user", managedUser);

        return "redirect:/post";
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable UUID id,HttpSession session) {
        postService.deletePostById(id);
        User userFromSession = (User) session.getAttribute("user");
        User user = userService.getById(userFromSession.getId());
        user.setPosts(postService.getAllPostsForUser(user));
        session.setAttribute("user", user);

        return "redirect:/post";
    }

    @PostMapping("/{id}/visibility")
    public String sharePost(@PathVariable UUID id) {
        postService.setPostVisibility(id, true);
        return "redirect:/post";
    }

    @GetMapping("/{id}")
    public ModelAndView readPost(@PathVariable UUID id, HttpSession session) {

        User user = (User) session.getAttribute("user");
        Post post = postService.findById(id);

        ModelAndView modelAndView = new ModelAndView("read-post");
        modelAndView.addObject("post", post);
        modelAndView.addObject("user", user);

        return modelAndView;
    }
}
