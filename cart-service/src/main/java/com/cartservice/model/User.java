package com.cartservice.model;

/**
 * Created by Achim Timis on 7/7/2016.
 */
public class User {

    private Long id;
    private String usernmame;
    private String password;


    public User(Long id, String usernmame, String password) {
        this.id = id;
        this.usernmame = usernmame;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (usernmame != null ? !usernmame.equals(user.usernmame) : user.usernmame != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (usernmame != null ? usernmame.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsernmame() {
        return usernmame;
    }

    public void setUsernmame(String usernmame) {
        this.usernmame = usernmame;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
