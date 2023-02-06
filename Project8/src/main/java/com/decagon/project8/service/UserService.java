package com.decagon.project8.service;


import com.decagon.project8.dto.TaskDTO;
import com.decagon.project8.dto.UserDTO;
import com.decagon.project8.model.Task;
import com.decagon.project8.model.User;

import java.util.List;

public interface UserService {
    User registerUser(UserDTO userdto);
    String loginUser(String email, String password);
    Task createdTask(TaskDTO taskDto);

    User getUserById(Integer id);

    Task updateTitleAndDescription(TaskDTO taskDTO, int id);
    Task getTaskById(int id);
    List<Task> viewAllTask();
    boolean updateTaskStatus(String status, int id);
    List<Task> viewAllTaskByStatus(String status, int  user_id);
    boolean deletedById(int id);
    User getUserByEmail(String email);


    String moveStatusForward(int id);

    String moveStatusBackwards(int id);


    List<Task> showTaskByUser(int id);
}
