package com.nickolasantunes.todosimple.services; 

import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nickolasantunes.todosimple.models.Task;
import com.nickolasantunes.todosimple.models.User;
import com.nickolasantunes.todosimple.repositories.TaskRepository;
import com.nickolasantunes.todosimple.services.exceptions.DataBindingViolationException;
import com.nickolasantunes.todosimple.services.exceptions.ObjectNotFoundException;

@Service
public class TaskService {
   
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new ObjectNotFoundException("ERROR: Tarefa não encontrada." + " Id: " + id + " Tipo: " + Task.class.getName()));
    }

    public List<Task> findAllByUserId(Long userId) {
        List<Task> tasks = this.taskRepository.findByUser_Id(userId);
        return tasks;
    }

    @Transactional
    public Task create(Task obj) {
        User user = this.userService.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newTask = findById(obj.getId());
        newTask.setDescription(obj.getDescription());
        return this.taskRepository.save(newTask);
    }

    @Transactional
    public void delete(Long id) {
        findById(id);
        try{
            taskRepository.deleteById(id);
        }
        catch(Exception ex){
            throw new DataBindingViolationException("Não é possivel deletar pois há entidades relacionadas");
        }
        
    }

}
