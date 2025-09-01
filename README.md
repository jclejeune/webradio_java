# WebRadioJava 🎵

Une application Java simple pour écouter des radios en ligne.  
Elle utilise VLC pour la lecture audio et Swing pour l’interface graphique.

---

## Fonctionnalités

- Liste dynamique de radios personnalisables.
- Lecture avec VLC (flux audio en direct).
- Changement de station propre (le flux précédent est arrêté automatiquement).
- Fermeture de la fenêtre stoppe le flux.
- Barre de statut affichant le nom de la radio et la note de musique "♪" quand la radio joue.

---

## Prérequis

- **Java 17+** installé et accessible via le PATH.
- **VLC** installé et accessible via le PATH.

---

## Installation des dépendances

### Windows (Scoop)
```powershell
# Installer Scoop si nécessaire
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
iwr -useb get.scoop.sh | iex

# Installer VLC
scoop install vlc
```

### Linux (Scoop)
sudo apt update
sudo apt install vlc

### Mac OS
# Installer Homebrew si nécessaire
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Installer VLC
brew install --cask vlc
