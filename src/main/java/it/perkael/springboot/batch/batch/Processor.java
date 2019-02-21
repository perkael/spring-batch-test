package it.perkael.springboot.batch.batch;

import it.perkael.springboot.batch.entity.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class Processor implements ItemProcessor<User, User> {

    private static final Map<String, String> DEPT_NAMES = new HashMap<>();

    public Processor(){
        DEPT_NAMES.put("001", "Technology");
        DEPT_NAMES.put("002", "Operations");
        DEPT_NAMES.put("003", "Accounts");
    }

    @Override
    public User process(User user) throws Exception {
        String deptCode = user.getDept();
        String dept = DEPT_NAMES.get(deptCode);
        user.setDept(dept);
        user.setDate(new Date());

        System.out.println("Process user from deptCode: "+deptCode+", to dept: "+dept);

        return user;
    }
}
