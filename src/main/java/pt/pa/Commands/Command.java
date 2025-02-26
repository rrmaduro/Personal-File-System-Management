package pt.pa.Commands;

import java.io.IOException;

public interface Command {
     void execute() throws IOException;

     void unexecute() throws IOException;
}
