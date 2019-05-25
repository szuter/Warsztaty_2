
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserMenagment {
    public static void main(String[] args) {
        String command = "";
        int userId = 0;
        Scanner scanner = new Scanner(System.in);
        UserDao userDao = new UserDao();
        while (!command.equalsIgnoreCase("quit")) {

            showAllUsers(userDao.findAll());
            System.out.println("Wybierz jedną z opcji (add, edit, delete, quit):");
            command = scanner.next();
            if (command.equalsIgnoreCase("add")) {
                userDao.create(addUser());
            } else if (command.equalsIgnoreCase("edit")) {
                System.out.println("Podaj id uzytkownika do edycji");
                userId = getUserId();
                if (hasUser(userId))
                    userDao.update(editUser(userDao.read(userId)), userId);


            } else if (command.equalsIgnoreCase("delete")) {
                System.out.println("Podaj id uzytkownika do usuniecia");
                userId = getUserId();
                if (hasUser(userId))
                    userDao.delete(userId);
            } else if (!command.equalsIgnoreCase("quit")) {
                System.out.println("Błąd! Wprowadz ponownie.");
            }
        }
    }

    private static void showAllUsers(List<User> users) {
        for (User user : users) {
            System.out.printf("Id: %s, Nazwa: %s, Email: %s, Password: %s, Id grupy: %s\n",
                    user.getId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getPassword(),
                    checkIfNull(user.getGroupId())
            );
        }
    }

    private static String checkIfNull(Integer integer) {
        if (integer == null)
            return "Brak";
        return Integer.toString(integer);

    }

    private static Integer getUserId() {
        Scanner scanner = new Scanner(System.in);
        Integer userId = null;
        while (userId == null)
            try {
                userId = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Błedna wartość!Podaj jeszcze raz.");
                scanner.next();
            }
        return userId;
    }

    private static User editUser(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ustaw id uzytkownika: ");
        int id = getUserId();
        while (hasUser(id)) {
            System.out.println("Podane Id juz istnieje.Podaj jeszcze raz.");
            id = getUserId();
        }
        user.setId(id);
        System.out.println("Ustaw nazwe uzytkownika: ");
        user.setUserName(scanner.nextLine());
        System.out.println("Ustaw email uzytkownika: ");
        user.setEmail(scanner.next());
        System.out.println("Ustaw hasło uzytkownika: ");
        user.setPassword(scanner.next());
        user.hashPassword(user.getPassword());
        System.out.println("Ustaw Id grupy uzytkownika");
        id = getUserId();
        while (!hasGroup(id)) {
            System.out.println("Podane Id grupy nie istnieje.Podaj jeszcze raz.");
            id = getUserId();
        }
        user.setGroupId(id);
        return user;

    }

    private static User addUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj nazwe uzytkownika: ");
        String userName = scanner.nextLine();
        System.out.println("Podaj email uzytkownika");
        String userEmail = scanner.next();
        System.out.println("Podaj hasło uzytkownika");
        String userPassword = scanner.next();
        System.out.println("Podaj id grupy uzytkownika");
        int groupId = getUserId();
        while (!hasGroup(groupId)) {
            System.out.println("Podane Id grupy nie istnieje.Podaj jeszcze raz.");
            groupId = getUserId();
        }
        return new User(userName, userEmail, userPassword, groupId);
    }

    private static Boolean hasUser(int userId) {
        UserDao userDao = new UserDao();
        if (userDao.read(userId) == null) {
            return false;
        }
        return true;
    }

    private static Boolean hasGroup(int groupId) {
        GroupDao groupDao = new GroupDao();
        if (groupDao.read(groupId) == null) {
            return false;
        }
        return true;
    }
}
