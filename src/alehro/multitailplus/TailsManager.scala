package alehro.multitailplus

import org.apache.commons.io.filefilter.RegexFileFilter
import java.util.regex.Pattern
import java.io.Writer
import scala.actors.Actor
import java.io.FileWriter
import java.io.BufferedWriter
import scala.collection.mutable.ListBuffer
import java.io.File
import org.apache.commons.io.input.Tailer
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.security.InvalidParameterException

case class PushString(logName:String, str:String)
case class AddOutput(regex:String, filePath:String)
case class AddInput(name:String, filePath:String)
       
    
object TailsManager extends Actor{
    val executors = Executors.newCachedThreadPool()
    class Output(val pattern:Pattern, val writer:Writer)//val is needed for to be public accessible!
    
    val listeners = new ListBuffer[Executor]
    
    val outs : ListBuffer[Output] = ListBuffer()
    def act{
        loop{
            react{
                case PushString(logName:String, str:String)=>pushString(logName, str)
                case AddOutput(regex:String, filePath:String)=>addOutput(regex, filePath)
                case AddInput(name:String,filePath:String)=>addInput(name,filePath)
            }
        }
    }
    
//    synchronized 
    private def pushString(logName:String,str:String){       
        for(out <- outs){
            if(out.pattern.matcher(str).matches()){                
                out.writer.append(" <<"+logName+">> "+str);
                out.writer.append("\n");
                out.writer.flush();
            }            
        }
    }
    //synchronized 
    private def addOutput(regex:String, filePath:String){
        val pat =  Pattern.compile(regex);
        val file  = new File(filePath)
        System.out.println("input file: "+file.getAbsoluteFile());
        val writer = new FileWriter(file, true);//append       
        val bwriter = new BufferedWriter(writer);
        outs+=new Output(pat, bwriter)
        //outs.add();

    }
    private def addInput(name:String, filePath:String){
        val listener = new FileTailListener();
        listener.logName = name
	    val file = new File(filePath);
	    if(!file.exists()){
	        System.out.println("Cannot find file: "+file.getAbsolutePath());
	        throw new InvalidParameterException("Cannot find file: "+file.getAbsolutePath())
	    }
	    System.out.println("input file: "+file.getAbsoluteFile());
	    val tailer = new Tailer(file, listener, 100, true);		   
	    executors.execute(tailer)	
	    System.out.println("listener started");
    }

}