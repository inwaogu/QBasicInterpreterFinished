package com.company;

public class BinaryTree {
    private String key;
    private String value;
    protected BinaryTree left, right;

    public BinaryTree(String key, String value)
    {
        this.key = key;
        this.value = value;
    }
    public BinaryTree()
    {

    }

    public String get( String key )
    {
        if (this.key.equals(key))
        {
            return value;
        }
        //(a > b) ? a : b; is an expression which returns one of two values, a or b. The condition, (a > b), is tested. If it is true the first value, a, is returned.
        // If it is false, the second value, b, is returned.
        // Whichever value is returned is dependent on the conditional test, a > b. The condition can be any expression which returns a boolean value.
        if (key.compareTo(this.key) < 0 )
        {
            return left == null ? null : left.get(key);
        }
        else
        {
            return right == null ? null : right.get(key);
        }
    }

    public void put(String key, String value)
    {
        if (key.compareTo(this.key) < 0)
        {
            if (left != null)
            {
                left.put(key, value);
            }
            else
            {
                left = new BinaryTree(key, value);
            }
        }
        else if (key.compareTo(this.key) > 0)
        {
            if (right != null)
            {
                right.put(key, value);
            }
            else
            {
                right = new BinaryTree(key, value);
            }
        }
        else
        {
            this.value = value;
        }
    }
}