import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable>{

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        public void map(Object key, Text value, Context context
                        ) throws IOException, InterruptedException {

            String line = value.toString();

            // Replaces all characters that are NOT alphanumeric or space with an empty string
            line = line.replaceAll("[^a-zA-Z0-9 ]", "");

            StringTokenizer itr = new StringTokenizer(line);

            // These variables shadow the class-level static variables defined above
            Text word = new Text();
            IntWritable one = new IntWritable(1);

            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }

    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
                           ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }


    public static void main(String[] args) throws Exception {
    // Capturing the start time
    long startTime = System.currentTimeMillis();

    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    // Setting the split size which is Question 9 requirement. The value is in bytes. 
    // Experimenting with small values (e.g., 100KB = 102400) to force more maps, or large values (e.g., 128MB = 134217728) for fewer maps.
    // Let's start with a small value to see the effect: 100KB
    job.getConfiguration().setLong("mapreduce.input.fileinputformat.split.maxsize", 102400);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    boolean success = job.waitForCompletion(true);

    // Capturing end time
    long endTime = System.currentTimeMillis();
    System.out.println("Total Execution Time: " + (endTime - startTime) + "ms");

    System.exit(success ? 0 : 1);
    }
}