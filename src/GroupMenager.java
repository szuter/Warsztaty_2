import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GroupMenager {
    public static void main(String[] args) {
        String command = "";
        int groupId = 0;
        Scanner scanner = new Scanner(System.in);
        GroupDao groupDao = new GroupDao();
        while (!command.equalsIgnoreCase("quit")) {
            showAllGroups(groupDao.findAll());
            System.out.println("Wybierz jedną z opcji (add, edit, delete, quit):");
            command = scanner.next();
            if (command.equalsIgnoreCase("add")) {
                groupDao.create(addGroup());
            } else if (command.equalsIgnoreCase("edit")) {
                System.out.println("Podaj id grupy do edycji");
                groupId = getId();
                if (hasGroup(groupId))
                    groupDao.update(editGroup(groupDao.read(groupId)), groupId);
                else
                    System.out.println("Brak grupy o tym Id");

            } else if (command.equalsIgnoreCase("delete")) {
                System.out.println("Podaj id grupy do usuniecia");
                groupId = getId();
                if (hasGroup(groupId))
                    groupDao.delete(groupId);
                else
                    System.out.println("Brak grupy o tym Id");
            } else if (!command.equalsIgnoreCase("quit")) {
                System.out.println("Błąd! Wprowadz ponownie.");
            }
        }
    }

    private static void showAllGroups(List<Group> groups) {
        for (Group group : groups) {
            System.out.printf("Id: %s, Nazwa grupy: %s\n",
                    group.getId(),
                    group.getName());
        }
    }


    private static Integer getId() {
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

    private static Group editGroup(Group group) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ustaw id grupy: ");
        int id = getId();
        while (hasGroup(id)) {
            System.out.println("Podane Id juz istnieje.Podaj jeszcze raz.");
            id = getId();
        }
        group.setId(id);
        System.out.println("Ustaw nazwe grupy: ");
        group.setName(scanner.nextLine());
        return group;

    }

    private static Group addGroup() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj nazwe grupy: ");
        String groupName = scanner.nextLine();
        return new Group(groupName);
    }

    private static Boolean hasGroup(int groupId) {
        GroupDao groupDao = new GroupDao();
        if (groupDao.read(groupId) == null) {
            return false;
        }
        return true;
    }
}

