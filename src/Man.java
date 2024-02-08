import java.util.List;

class Man {
    private String name;
    private int age;
    private List<String> favoriteBooks;
    private Man bestFriend;

    public Man(String name, int age, List<String> favoriteBooks, Man bestFriend) {
        this.name = name;
        this.age = age;
        this.favoriteBooks = favoriteBooks;
        this.bestFriend = bestFriend;
    }
    public Man(String name, int age, List<String> favoriteBooks) {
        this.name = name;
        this.age = age;
        this.favoriteBooks = favoriteBooks;
        this.bestFriend = null;
    }

    public Man getBestFriend() {
        return bestFriend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void setFavoriteBooks(List<String> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }

    public void setBestFriend(Man bestFriend) {
        this.bestFriend = bestFriend;
    }

    @Override
    public String toString() {
        return "Man{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", favoriteBooks=" + favoriteBooks +
                ", bestFriend=" + bestFriend +
                '}';
    }
}