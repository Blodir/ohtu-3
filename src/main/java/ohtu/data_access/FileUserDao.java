/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.data_access;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ohtu.domain.User;
import org.json.JSONObject;

/**
 *
 * @author Pyry
 */
public class FileUserDao implements UserDao {

    File userDocument;

    public FileUserDao(String filename) {
        userDocument = new File(filename);
    }

    @Override
    public List<User> listAll() {
        List<User> userList = new ArrayList();
        Scanner reader = null;
        try {
            reader = new Scanner(userDocument);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (reader.hasNextLine()) {
            JSONObject j = new JSONObject(reader.nextLine());
            userList.add(new User(j.getString("username"), j.getString("password")));
        }
        reader.close();
        return userList;
    }

    @Override
    public User findByName(String name) {
        User user = null;
        Scanner reader = null;
        try {
            reader = new Scanner(userDocument);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (reader.hasNextLine()) {
            JSONObject u = new JSONObject(reader.nextLine());
            if (u.getString("username").equals(name)) {
                user = new User(u.getString("username"), u.getString("password"));
                break;
            }
        }
        reader.close();
        return user;
    }

    @Override
    public void add(User user) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(userDocument, true);
        } catch (IOException ex) {
            Logger.getLogger(FileUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSONObject json = new JSONObject();
        json.put("username", user.getUsername());
        json.put("password", user.getPassword());

        writer.write(json.toString() + "\n");
        try {
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(FileUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(FileUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
