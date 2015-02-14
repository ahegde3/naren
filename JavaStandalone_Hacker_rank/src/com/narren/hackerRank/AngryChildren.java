package com.narren.hackerRank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

// The part of the program involving reading from STDIN and writing to STDOUT has been provided by us.

public class AngryChildren {
   static BufferedReader in = new BufferedReader(new InputStreamReader(
         System.in));
   static StringBuilder out = new StringBuilder();

   public static void main(String[] args) throws NumberFormatException, IOException {
      int numPackets = Integer.parseInt(in.readLine());
      int numKids = Integer.parseInt(in.readLine());
      int[] packets = new int[numPackets];
      
      for(int i = 0; i < numPackets; i ++)
      {
         packets[i] = Integer.parseInt(in.readLine());
      }
      Arrays.sort(packets);
      int unfairness = Integer.MAX_VALUE;
      for (int i = 0; i <  packets.length; i++) {
    	  if (packets.length < i + (numKids)) {
    		  break;
    	  }
    	  int value = (packets[i + (numKids - 1)] - packets[i]);
    	  unfairness = unfairness > value ? value : unfairness;
      }
      
        // Write your code here, to process numPackets N, numKids K, and the packets of candies
      // Compute the ideal value for unfairness over here
      
      System.out.println(unfairness);
   }
}