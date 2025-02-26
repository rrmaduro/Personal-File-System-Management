package pt.pa.Backup;

import pt.pa.Backup.Memento;

public interface Originator {
    public Memento saveState();

    public void setState(Memento savedMemento);
}
