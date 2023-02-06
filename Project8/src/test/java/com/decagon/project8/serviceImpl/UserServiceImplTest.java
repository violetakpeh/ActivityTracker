package com.decagon.project8.serviceImpl;


import com.decagon.project8.dto.TaskDTO;
import com.decagon.project8.dto.UserDTO;
import com.decagon.project8.model.Status;
import com.decagon.project8.model.Task;
import com.decagon.project8.model.User;
import com.decagon.project8.repository.TaskRepository;
import com.decagon.project8.repository.UserRepository;
import com.decagon.project8.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.Month.AUGUST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    private UserService userService;

    private Task task;
    private User user;
    private TaskDTO taskDTO;
    private UserDTO userDTO;
    private LocalDateTime dateTime;
    List<Task> taskList;

    @BeforeEach
    void setUp() {
        userServiceImpl = new UserServiceImpl(userRepository, taskRepository);

        dateTime = LocalDateTime.of(2022, AUGUST,3,6,30,40,50000);
        taskList = new ArrayList<>();
        taskList.add(task);

        user= new User();
        user.setId(1);
        user.setFullName("may june");
        user.setEmail("june@gmail.com");
        user.setPassword("1234");
        user.setTasks(taskList);

        task = new Task(1, "my task", "trying out new task", Status.PENDING, dateTime, dateTime, dateTime, user);
        task.setId(1);
        task.setTitle("my task");
        task.setDescription("trying out new task");
        task.setStatus(Status.PENDING);
        task.setCreatedAt(dateTime);
        task.setUpdatedAt(dateTime);
        task.setCompletedAt(dateTime);
        task.setUser(user);


        taskDTO = new TaskDTO();
        taskDTO.setTitle("read up for the task");
        taskDTO.setDescription("read up for the task");
        taskDTO.setUser_id(1);


        userDTO = new UserDTO("may june", "june@gmail.com", "1234");
        userDTO.setFullName("may june");
        userDTO.setEmail("june@gmail.com");
        userDTO.setPassword("1234");



    }

        @Test
        void registerUser() {
        User expected = userServiceImpl.registerUser(userDTO);
        User actual = userRepository.save(user);
        assertEquals(expected , actual);
        }

        @Test
        void tologinUser_Successfull() {
            when(userServiceImpl.loginUser("june@gmail.com", "1234")).thenReturn("success");
            assertEquals("success" , userServiceImpl.loginUser("june@gmail.com", "1234"));

        }

        @Test
        void loginUser_Unsuccessfull() {
            String message = "incorrect password";
            assertEquals(message , userServiceImpl.loginUser("enwerevincent@gmail.com" , "1234"));
        }


        @Test
        void createTask() {
            userServiceImpl.createdTask(taskDTO);
            Task task1 = new Task();
            task1.setTitle(taskDTO.getTitle());
            task1.setDescription(taskDTO.getDescription());
            task1.setStatus(Status.PENDING);
            task1.setDescription(taskDTO.getDescription());
           userService.getUserById(taskDTO.getUser_id());
            verify(taskRepository).save(task1);
        }

        @Test
        void updateTitleAndDescription() {
            assertEquals(task , userServiceImpl.updateTitleAndDescription(taskDTO , 1));
        }

        @Test
        void viewAllTasks() {
            assertEquals(1 , userServiceImpl.viewAllTask().size());
        }

        @Test
        void viewAllTaskByStatus() {

            assertEquals(taskList , userServiceImpl.viewAllTaskByStatus("pending", 1));
        }

        @Test
        void updateTaskStatus() {
            assertTrue(userServiceImpl.updateTaskStatus("ongoing" , 1));
        }

        @Test
        void getUserByEmail() {

        userServiceImpl.getUserByEmail("june@gmail.com");
            verify(userRepository).findUserByEmail(user.getEmail());
        }

        @Test
        void getTaskById() {
            assertEquals(task, userServiceImpl.getTaskById(1));
        }
    }
