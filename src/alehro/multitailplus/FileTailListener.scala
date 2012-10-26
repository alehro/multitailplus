package alehro.multitailplus

import org.apache.commons.io.input.TailerListenerAdapter

class FileTailListener extends TailerListenerAdapter {
    var logName = ""
    override def handle(line:String) {
        TailsManager ! PushString(logName, line);
        //System.out.println(line);
    }
}