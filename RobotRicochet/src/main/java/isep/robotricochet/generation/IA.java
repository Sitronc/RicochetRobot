package isep.robotricochet.generation;

public class IA {
    //test IA pour resoudre l'énigme du ricochet robot, on va se baser sur un projet réalisé qui a pour but de déterminer le nombre de combinaison possible pour atteindre 100 euros
    //Grace à un algo de parcours d'arbre (DFS)

        public static void main(String[] args) {

            int[] piece = {200, 100, 50, 20, 10, 5, 2, 1};
            combinaison(10000, piece, 0);
        }

        public static int combinaison(int montant, int[] pieces, int index) {
            if (montant == 0)
                return 1;
            if (index == pieces.length - 1)
                return montant % pieces[index] == 0 ? 1 : 0;
            int total = 0;
            for (int i = 0; i * pieces[index] <= montant; i++) {
                total += combinaison(montant - i * pieces[index], pieces, index + 1);
            }
            return total;
        }
}
