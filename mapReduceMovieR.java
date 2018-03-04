package WordCount.WordCount;

import java.io.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class mapReduceMovieR {

  public static class RatingMapper 
       extends Mapper<Object, Text, Text, DoubleWritable>{

    private Text word = new Text();
    private DoubleWritable rating=new DoubleWritable();
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	String line = value.toString();
    	if(line.charAt(0)!='u') 
    	{
	        String[] line_values = line.split(",");
	        word.set(line_values[1]);
	        rating.set(Double.parseDouble(line_values[2]));
	        context.write(word, rating);
    	}
    }
  }

  public static class AverageReducer
       extends Reducer<Text,DoubleWritable,Text,DoubleWritable> {
    private DoubleWritable result = new DoubleWritable();

    public void reduce(Text key, Iterable<DoubleWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      double sum=0.0;
      int count=0;
      for (DoubleWritable val : values) {
        sum += val.get();
        count++;
      }
      result.set(sum/count);
      context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    conf.set("mapred.job.tracker", "hdfs://cshadoop1:61120");
    conf.set("yarn.resourcemanager.address", "cshadoop1.utdallas.edu:8032");
    conf.set("mapreduce.framework.name", "yarn");
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(mapReduceMovieR.class);
    job.setMapperClass(RatingMapper.class);
    job.setReducerClass(AverageReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(DoubleWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}