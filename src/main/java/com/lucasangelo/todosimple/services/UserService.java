package com.lucasangelo.todosimple.services;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lucasangelo.todosimple.models.User;
import com.lucasangelo.todosimple.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;



    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id); 

        return user.orElseThrow(() -> new RuntimeException("Usuário não encontrado. Id:" + id + ", Tipo: " +  User.class.getName()));
    }

    @Transactional
    public User create(User obj) {
        try{
            obj.setId(null);
            this.userRepository.save(obj);
            return obj;
        }
        catch(DataIntegrityViolationException dataIntegrityViolationException){
            throw new RuntimeException("ERRO usúario já existente.");
        }
    }

    @Transactional
    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        return this.userRepository.save(newObj);
    }

  
    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possivel excluir pois há entidades relacionadas!");
        }
    }   



}
