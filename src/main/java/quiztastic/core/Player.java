package quiztastic.core;

public class Player {

    String name;

    public Player() {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Player " + name;
    }

}

