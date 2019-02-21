package it.perkael.springboot.batch.batch;

import it.perkael.springboot.batch.entity.User;
import it.perkael.springboot.batch.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DBWriter implements ItemWriter<User> {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public void write(List<? extends User> users) throws Exception {
        System.out.println("Write users on db: " + users);
        userRepository.saveAll(users);
    }
}
