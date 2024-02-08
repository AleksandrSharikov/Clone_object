import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Man peter = new Man("Peter", 25, new ArrayList<>(List.of("Murzilka", "Voynna i mir", "Bukvar"))
                , new Man("Ignat", 45, null) );
        Man ivan = CopyUtils.deepCopy(peter);



        System.out.println("Check if copy is done");
        System.out.println(peter);
        System.out.println(ivan);

        ivan.setName("Ivan");
        ivan.getFavoriteBooks().add("MuMu");
        ivan.getBestFriend().setName("Ponteleymon");
        ivan.setAge(18);
        peter.getFavoriteBooks().remove("Murzilka");


        System.out.println("Check if copies are independent");
        System.out.println(peter);
        System.out.println(ivan);

    }
}