import java.awt.*;
import java.util.Random;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Pokemon {
    String nom;
    int PV;
    int degats;
    int soin;
    String attaque;
    String Soins;

    public Pokemon(String nom, int PV, int degats, int soin, String attaque, String Soins) {
        this.nom = nom;
        this.PV = PV;
        this.degats = degats;
        this.soin = soin;
        this.attaque = attaque;
        this.Soins = Soins;
    }
}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            // Charge l'image de fond
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dessine l'image de fond
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

public class JeuGraphique {
    private static Pokemon joueur1;
    private static Pokemon joueur2;
    private static JFrame frame;
    private static BackgroundPanel panel;
    private static JButton attaqueButton, soinButton;
    private static JLabel infoLabel, joueur1Label, joueur2Label;
    private static Random random;
    private static JTextArea gameLog;

    public static void main(String[] args) {
        random = new Random();

        frame = new JFrame("Combat Pokémon");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Remplacer le JPanel par notre BackgroundPanel
        panel = new BackgroundPanel("C:\\Users\\bt412090\\OneDrive - Université Côte d'Azur\\Bureau\\Java\\JeuGraphique.java\\fondjungle.jfif");  // Remplacez par le chemin de votre image
        panel.setLayout(new BorderLayout());

        infoLabel = new JLabel("Bienvenue dans le Combat Ultime!", JLabel.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(infoLabel, BorderLayout.NORTH);

        gameLog = new JTextArea(10, 40);
        gameLog.setEditable(false);
        panel.add(new JScrollPane(gameLog), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        attaqueButton = new JButton("Attaquer");
        soinButton = new JButton("Se Soigner");
        buttonPanel.add(attaqueButton);
        buttonPanel.add(soinButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        joueur1Label = new JLabel("Joueur 1 (PV): 0");
        joueur2Label = new JLabel("Joueur 2 (PV): 0");
        JPanel pvPanel = new JPanel();
        pvPanel.add(joueur1Label);
        pvPanel.add(joueur2Label);
        panel.add(pvPanel, BorderLayout.WEST);

        frame.add(panel);
        frame.setVisible(true);

        choisirPersonnage();

        attaqueButton.addActionListener(e -> attaquer());
        soinButton.addActionListener(e -> soigner());
    }

    public static void verifierFinJeu() {
        if (joueur1.PV <= 0) {
            gameLog.append(joueur1.nom + " a perdu ! " + joueur2.nom + " gagne !\n");
            finDuJeu();
        } else if (joueur2.PV <= 0) {
            gameLog.append(joueur2.nom + " a perdu ! " + joueur1.nom + " gagne !\n");
            finDuJeu();
        }
    }

    public static void finDuJeu() {
        attaqueButton.setEnabled(false);
        soinButton.setEnabled(false);
        JOptionPane.showMessageDialog(frame, "Le combat est terminé !", "Fin du Jeu", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void choisirPersonnage() {
        String[] options = {"Dracofeu", "Asterion", "Pikachu", "Bulbizarre"};
        int choix = JOptionPane.showOptionDialog(frame, "Choisissez votre personnage", "Choix du Pokémon",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choix) {
            case 0 -> joueur1 = new Pokemon("Dracofeu", 250, 35, 10, "Tempête Écaratée", "Bain de Magma");
            case 1 -> joueur1 = new Pokemon("Asterion", 180, 12, 5, "Touche pas à mon bios", "Esquive sur Twitter");
            case 2 -> joueur1 = new Pokemon("Pikachu", 100, 22, 15, "Queue de Fer", "Surcharge");
            case 3 -> joueur1 = new Pokemon("Bulbizarre", 350, 15, 20, "Vampire Végétal", "Photosynthèse");
        }

        joueur2 = choisirPokemonAdverse();
        updateLabels();
    }

    public static Pokemon choisirPokemonAdverse() {
        int choixPokemon = random.nextInt(4);
        return switch (choixPokemon) {
            case 0 -> new Pokemon("Dracofeu", 250, 35, 10, "Tempête Écaratée", "Bain de Magma");
            case 1 -> new Pokemon("Asterion", 180, 12, 5, "Touche pas à mon bios", "Esquive sur Twitter");
            case 2 -> new Pokemon("Pikachu", 100, 22, 15, "Queue de Fer", "Surcharge");
            case 3 -> new Pokemon("Bulbizarre", 350, 15, 20, "Vampire Végétal", "Photosynthèse");
            default -> null;
        };
    }

    public static void updateLabels() {
        joueur1Label.setText("Joueur 1 (" + joueur1.nom + "): " + joueur1.PV + " PV");
        joueur2Label.setText("Joueur 2 (" + joueur2.nom + "): " + joueur2.PV + " PV");
    }

    public static void attaquer() {
        joueur2.PV -= joueur1.degats;
        gameLog.append(joueur1.nom + " attaque " + joueur2.nom + " avec " + joueur1.attaque + " ! " + joueur2.nom + " perd " + joueur1.degats + " points de vie.\n");
        updateLabels();
        verifierFinJeu();
        if (joueur2.PV > 0) actionAleatoireJoueur2();
    }

    public static void soigner() {
        joueur1.PV += joueur1.soin;
        gameLog.append(joueur1.nom + " utilise " + joueur1.Soins + " et récupère " + joueur1.soin + " points de vie.\n");
        updateLabels();
        verifierFinJeu();
        if (joueur2.PV > 0) actionAleatoireJoueur2();
    }

    public static void actionAleatoireJoueur2() {
        int choixAction = random.nextInt(2);
        if (choixAction == 0) {
            joueur1.PV -= joueur2.degats;
            gameLog.append(joueur2.nom + " attaque " + joueur1.nom + " avec " + joueur2.attaque + " ! " + joueur1.nom + " perd " + joueur2.degats + " points de vie.\n");
        } else {
            joueur2.PV += joueur2.soin;
            gameLog.append(joueur2.nom + " utilise " + joueur2.Soins + " et se soigne de " + joueur2.soin + " points de vie.\n");
        }
        updateLabels();
        verifierFinJeu();
    }
}
