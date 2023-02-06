package com.decagon.project8.serviceImpl;


import com.decagon.project8.dto.TaskDTO;
import com.decagon.project8.dto.UserDTO;
import com.decagon.project8.exception.TaskNotFoundException;
import com.decagon.project8.exception.UserNotFoundException;
import com.decagon.project8.model.Status;
import com.decagon.project8.model.Task;
import com.decagon.project8.model.User;
import com.decagon.project8.repository.TaskRepository;
import com.decagon.project8.repository.UserRepository;
import com.decagon.project8.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public User registerUser(UserDTO userdto){
        User user = new User();
        user.setFullName(userdto.getFullName());
        user.setEmail(userdto.getEmail());
        user.setPassword(userdto.getPassword());
     return   userRepository.save(user);
    }

    @Override
   public String loginUser(String email, String password){
      String message = "";
      User user = getUserByEmail(email);
      if(user.getPassword().equals(password)){
          message = "success";
      }
      else{
          message= "incorrect password";
      }
      return message;
    }
    @Override
   public Task createdTask(TaskDTO taskDto){
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(Status.PENDING);
        task.setDescription(taskDto.getDescription());
        User loginUser = getUserById(taskDto.getUser_id());
        task.setUser(loginUser);
       return taskRepository.save(task);
    }
    @Override
    public User getUserById(Integer id){
        return userRepository.findById(id).orElseThrow(()->new TaskNotFoundException("Task not found"));

    }
    @Override
   public Task updateTitleAndDescription(TaskDTO taskDTO, int id){
        Task task = getTaskById(id);
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        return taskRepository.save(task);
    }
    @Override
   public Task getTaskById(int id){
        return taskRepository.findById(id).orElseThrow(()->new TaskNotFoundException("Task not found"));

    }
    @Override
   public List<Task> viewAllTask(){
        return taskRepository.findAll();
    }
    @Override
   public boolean updateTaskStatus(String status, int id){
        return taskRepository.updateTaskByIdAndStatus(status, id);
    }

    @Override
    public List<Task> viewAllTaskByStatus(String status, int user_id){
        return taskRepository.listOfTasksByStatus(status);
    }

    @Override
   public boolean deletedById(int id){
        taskRepository.deleteById(id);
        return true;
    }
    @Override
    public User getUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElseThrow(()-> new UserNotFoundException(email + "not found"));
    }
    @Override
    public String moveStatusForward(int id){
        String message = "";
        Task task = taskRepository.findById(id).get();
        if(task.getStatus().equals(Status.PENDING)){
            task.setStatus(Status.IN_PROGRESS);
            taskRepository.save(task);
            message = "in progress";
        }
        else if(task.getStatus().equals(Status.IN_PROGRESS)){
            task.setStatus(Status.COMPLETED);
            task.setCompletedAt(LocalDateTime.now());
            taskRepository.save(task);

            message = " completed";
        }
        else{
            message = "unauthorized move";
        }
        return message;
    }
@Override
    public String moveStatusBackwards(int id){
        String message = "";
        Task task = taskRepository.findById(id).get();
        if(task.getStatus().equals(Status.IN_PROGRESS)){
            task.setStatus(Status.PENDING);
            taskRepository.save(task);
            message = "pending";
        }
        else { message = " unauthorized move";
        }
        return message;
    }
@Override
public List<Task> showTaskByUser(int id){
        return taskRepository.listOfTasksByUserId(id);
}

}
