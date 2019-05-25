import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserApp {
    public static void main(String[] args) {
        Integer userId = Integer.parseInt(args[0]);
        SolutionDao solutionDao = new SolutionDao();
        if (idMatch(userId)) {
            String command = "";
            Scanner scanner = new Scanner(System.in);
            while (!command.equalsIgnoreCase("quit")) {
                System.out.println("Wybierz jedną z opcji (add, quit):");
                command = scanner.next();
                if (command.equalsIgnoreCase("add")) {
                    List<Solution> solutions = solutionDao.findAllByUserId(userId);
                    if (hasExercises(solutions)) {
                        showExercise(solutions);
                        addSolution(solutions);
                    } else
                        System.out.println("Brak przypisanych zadan");
                } else if (!command.equalsIgnoreCase("quit")) {
                    System.out.println("Błąd! Wprowadz ponownie.");
                }
            }
        } else
            System.out.println("Brak uzytkownika o tym Id.");
    }

    private static void addSolution(List<Solution> solutions) {
        Scanner scanner = new Scanner(System.in);
        int temp = 0;
        for (Solution sol : solutions) {
            if (sol.getDescription() == null) {
                System.out.println("Podaj id zadania: ");
                SolutionDao solutionDao = new SolutionDao();
                while (temp == 0) {
                    int exerciseId = getId();
                    for (Solution solution : solutions) {
                        if (solution.getExercise_id() == exerciseId) {
                            System.out.println("Podaj odpowiedz na zadanie: ");
                            solution.setDescription(scanner.nextLine());
                            solutionDao.update(solution);
                            temp++;
                            break;
                        }
                    }
                    if (temp == 0)
                        System.out.println("Podano złe Id zadania. Podaj ponownie.");
                }
                break;
            }
        }
        if (temp == 0)
            System.out.println("Brak przypisanych zadan");
    }

    private static boolean hasExercises(List<Solution> solutions) {
        if (solutions != null)
            return true;
        return false;
    }

    private static void showExercise(List<Solution> solutions) {
        ExerciseDao exerciseDao = new ExerciseDao();
        for (Solution solution : solutions) {
            if (solution.getDescription() == null) {
                System.out.printf("Id: %s, Tytuł: %s, Opis: %s\n",
                        exerciseDao.read(solution.getExercise_id()).getId(),
                        exerciseDao.read(solution.getExercise_id()).getTitle(),
                        exerciseDao.read(solution.getExercise_id()).getDescription());
            }
        }
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

    private static Boolean idMatch(Integer userId) {
        UserDao userDao = new UserDao();
        List<User> users = userDao.findAll();
        for (User user : users) {
            if (user.getId() == userId)
                return true;
        }
        return false;
    }

}
