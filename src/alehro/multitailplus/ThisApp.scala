package alehro.multitailplus
import java.io.File
import org.apache.commons.io.input.Tailer
import java.util.concurrent.Executor
import com.codahale.jerkson.Json._

case class OutConfig(pattern:String, file:String)
case class InConfig(name:String, file:String)
case class WholeConfig(in:List[InConfig], out:List[OutConfig])
    
object ThisApp{
    def main(args: Array[String]) {
    
	    if(args.length<1){
	        System.out.println("Please specify config file path.");
	        return
	    }
	    val s1:String = args(0)
	    //val configFile = new File("d:\\oDesk\\wisestamp\\eclipsewsp\\multitailplus\\work\\config.json");
	    //val configLines = scala.io.Source.fromFile("d:\\oDesk\\wisestamp\\eclipsewsp\\multitailplus\\work\\config.json").mkString
	    val configLines = scala.io.Source.fromFile(args(0)).mkString
	    val config = parse[WholeConfig](configLines);
	    TailsManager.start()
	    for(in1  <- config.in){
	        TailsManager ! AddInput(in1.name, in1.file)
	    }
	    for(out  <- config.out){
	    	//TailsManager ! AddOutput(".*(INFO|ERROR).*", "d:\\oDesk\\wisestamp\\logs\\log1.log");
	        TailsManager ! AddOutput(out.pattern, out.file);
	    }    
    }
}