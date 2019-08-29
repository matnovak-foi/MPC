package org.foi.mpc.executiontools.calibrator;

public class SherlockCalibraorConfig {
    //TODO move that params to config file not good that it is hard coded can be a different config file
    public int[] maxFwdJmps ={1,2,3};//TEXT//{8,11,15};//{7,8,9,10,11,13,15,17,19};//{1,3,4,8,10};
    //TOK//{4, 11, 12, 17, 19, 5, 7, 10};//{3, 4, 11, 15, 18};
    public int[] minRunLengths = {1,2,3,4};//TEXT//{1,2,3,4,5,6,10,12,15};//{1,2,3,4,5,6};//{1,2,3,5,8,14};
    //TOK//{7, 14, 4, 6, 8, 10, 13, 15};//{14, 9, 7, 5, 3};
    public int[] maxJmpDiffs = {1,2,3,4};//TEXT//{1,5,10,15};//{1};//{1,2};
    //TOK//{1, 2, 5, 6, 7};//{1, 2, 3, 4, 5};
    public int[] minStrLens = {2};//TEXT//{1,2,5};//{1,14,17};
    //TOK//{1,2};//{1, 2};
    public int[] strictness_s = {3};//TEXT//{2};//{1,2,3,4};//{1,3,7}
    //TOK//{1};//{2, 1};
    public int maxBwdJmp = 1;
}
