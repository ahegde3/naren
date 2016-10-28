package com.narren.hackerRank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class PGraph
{    int weight[][]=new int[20][20];
    int visited[]=new int [20];
    int d[]=new int[20];
    int p[]=new int[20];
    int v,e;
    void creategraph()throws IOException
    {
        int i,j,a,b,w;
                     BufferedReader in=new BufferedReader( new InputStreamReader(System.in));
        System.out.print("\nEnter number of vertices :");
         v=Integer.parseInt(in.readLine());
        System.out.print("\nEnter number of Edges :");
         e=Integer.parseInt(in.readLine());
        for ( i=1;i<=v;i++)
            for( j=1;j<=v;j++)
                weight[i][j]=0;

        for (i=1;i<=v;i++)
            {
            p[i]=visited[i]=0;
            d[i]=32767;
            }
        for ( i=1;i<=e;i++)
        {
        System.out.print("\nEnter edge a,b and weight w :");
         a=Integer.parseInt(in.readLine());
         b=Integer.parseInt(in.readLine());
         w=Integer.parseInt(in.readLine());
        weight[a][b]=weight[b][a]=w;
        }

    }

     void algo ()throws IOException
    {
     creategraph();
      int current,total,mincost,i;
      current=1;d[current]=0;
       total=1;
       visited[current]=1;
       while(total!=v)
        {
          for (i=1;i<=v;i++)
           {  
         if(weight[current][i]!=0)
          if(visited[i]==0)
          if(d[i]>weight[current][i])
                            {
               d[i]=weight[current][i];
                p[i]=current;
               }
                    }
    mincost=32767;
      for (i=1;i<=v;i++)
        {
            if(visited[i]==0)
            if(d[i]<mincost)
        {
        mincost=d[i];
        current=i;
           }
      }
    visited[current]=1;
    total++;
       }
    mincost=0;
    for(i=1;i<=v;i++)
    mincost=mincost+d[i];
    System.out.print("\n Minimum cost="+mincost);
    System.out.print("\n Minimum Spanning tree is");

    for(i=1;i<=v;i++)
    System.out.print("\n vertex" +i+"is connected to"+p[i]);
}
}
public class Prims
{
public static void main(String args[])throws IOException
{
    PGraph g = new PGraph();
    g.algo();
}
}