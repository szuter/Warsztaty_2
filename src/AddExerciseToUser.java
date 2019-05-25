import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AddExerciseToUser {
    public static void main(String[] args) {

        String command = "";
        Scanner scanner = new Scanner(System.in);
        while (!command.equalsIgnoreCase("quit")) {
            System.out.println("Wybierz jedną z opcji (add, view, quit):");
            command = scanner.next();
            if (command.equalsIgnoreCase("add")) {
                addExerciseToUser();
            } else if (command.equalsIgnoreCase("view")) {
                System.out.println("Podaj id uzytkownika: ");
                int userId = getId();
                if (hasUser(userId))
                    viewUser(userId);
                else
                    System.out.println("Brak uzytkowanika o tym Id");
            } else if (!command.equalsIgnoreCase("quit")) {
                System.out.println("Błąd! Wprowadz ponownie.");
            }
        }
    }

    private static void viewUser(int userId) {
        SolutionDao solutionDao = new SolutionDao();
        List<Solution> solutions = solutionDao.findAllByUserId(userId);
        for (Solution solution : solutions) {
            if (solution.getUpdated() == null && solution.getDescription() == null)
                System.out.printf("Id zadania: %s, Utwozono: %s, Aktualizowano: Brak, Opis: Brak\n", solution.getExercise_id(), solution.getCreated());
            else
                System.out.printf("Id zadania: %s, Utwozono: %s, Aktualizowano: %s, Opis: %s\n", solution.getExercise_id(), solution.getCreated(), solution.getUpdated(), solution.getDescription());
        }
    }

    private static void addExerciseToUser() {
        Solution solution = new Solution();
        UserDao userDao = new UserDao();
        ExerciseDao exerciseDao = new ExerciseDao();
        SolutionDao solutionDao = new SolutionDao();
        showAllUsers(userDao.findAll());
        System.out.println("Podaj id uzytkownika: ");
        int id = getId();
        while (!hasUser(id)) {
            System.out.println("Podane Id nie istnieje.Podaj jeszcze raz.");
            id = getId();
        }
        solution.setUser_id(id);
        showAllExercises(exerciseDao.findAll());
        System.out.println("Podaj id zadania: ");
        id = getId();
        while (!hasExercise(id)) {
            System.out.println("Podane Id nie istnieje.Podaj jeszcze raz.");
            id = getId();
        }
        solution.setExercise_id(id);
        if (userHasExercise(solution.getExercise_id(), solution.getUser_id()))
            solutionDao.create(solution);


    }

    private static boolean userHasExercise(int exercise_id, int user_id) {
        SolutionDao solutionDao = new SolutionDao();
        List<Solution> solutions = solutionDao.findAllByUserId(user_id);
        for (Solution solution : solutions){
            if (solution.getExercise_id()==exercise_id) {
                System.out.println("Uzytkownika posiada juz zadanie o tym id");
                return false;
            }
        }
        return true;
    }

    private static Boolean hasExercise(int exerciseId) {
        ExerciseDao exerciseDao = new ExerciseDao();
        if (exerciseDao.read(exerciseId) == null) {
            return false;
        }
        return true;
    }

    private static Boolean hasUser(int userId) {
        UserDao userDao = new UserDao();
        if (userDao.read(userId) == null) {
            return false;
        }
        return true;
    }

    private static Integer getId() {
        Scanner scanner = new Scanner(System.in);
        Integer id = null;
        while (id == null)
            try {
                id = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Błedna wartość!Podaj jeszcze raz.");
                scanner.next();
            }
        return id;
    }

    private static void showAllUsers(List<User> users) {
        for (User user : users) {
            System.out.printf("Id: %s, Nazwa: %s, Email: %s, Password: %s\n", user.getId(), user.getUserName(), user.getEmail(), user.getPassword());
        }
    }

    private static void showAllExercises(List<Exercise> exercises) {
        for (Exercise exercise : exercises) {
            System.out.printf("Id: %s, Tytuł: %s, Opis: %s\n", exercise.getId(), exercise.getTitle(), exercise.getDescription());
        }
    }
}
