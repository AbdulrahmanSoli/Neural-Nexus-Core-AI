import java.util.*;
import java.io.*;

public class Solution {

    static Scanner sc = new Scanner(System.in);
    static Index index = new Index();

    public static void main(String[] args) throws Exception {
        while (sc.hasNext()) {
            if (!sc.hasNextInt()) {
                System.out.println(-1); 
                sc.next(); 
                continue; 
            }
            int option = sc.nextInt();
            
            if (option < 1 || option > 4) {
                 System.out.println(-1);
                 if (sc.hasNextLine()) sc.nextLine();
                 continue;
            }
            command(option);
        }
    }

    static void command(int n) throws Exception {
        switch (n) {
            case 1:
                addFiles();
                break;
            case 2:
                search();
                break;
            case 3:
                delete();
                break;
            case 4:
                print();
                break;
            default:
                System.out.println(-1); 
        }
    }

    static void addFiles() throws Exception {
        if (!sc.hasNextInt()) { 
            System.out.println(-1);
            if (sc.hasNextLine()) sc.nextLine();
            return;
        }
        int fileCount = sc.nextInt(); 

        if (fileCount < 1 || fileCount > 100000) {
            System.out.println(-1);
            if (sc.hasNextLine()) sc.nextLine(); 
            return; 
        }

        for (int i = 0; i < fileCount; i++) {
            String name = sc.next();
            
            if (!sc.hasNextInt()) { 
                 System.out.println(-1);
                 if (sc.hasNextLine()) sc.nextLine();
                 return; 
            }
            int lines = sc.nextInt(); 

            if (lines < 1 || lines > 100) {
                System.out.println(-1);
                if (sc.hasNextLine()) sc.nextLine();
                return; 
            }
            
            sc.nextLine(); 

            String[][] arr = new String[lines][];
            for (int j = 0; j < lines; j++) {
                if (sc.hasNextLine()) {
                    arr[j] = sc.nextLine().trim().split("\\s+");
                } else {
                    arr[j] = new String[0]; 
                }
            }

            for (int j = 0; j < lines; j++) {
                for (int k = 0; k < arr[j].length; k++) {
                    if (arr[j][k].isEmpty()) continue; 
                    
                    String lowerToken = arr[j][k].toLowerCase();
                    
                    AVLNode node = index.insert(lowerToken); 
                    
                    if (node != null) {
                         node.list.insert(new Node(name, j + 1));
                         node.frequency++;
                    }
                }
            }
        }
    }

    static void search() {
        if (!sc.hasNext()) {
             System.out.println(-1); 
             return;
        }
        String token = sc.next().toLowerCase();
        
        AVLNode node = index.search(token);
        if (node == null) {
            System.out.println(-1); 
            return;
        }
        System.out.println(node.frequency);
        if (node.frequency > 0) {
            System.out.println(node.list);
        }
    }

    static void delete() {
         if (!sc.hasNext()) {
             System.out.println(-1); 
             return; 
         }
        String token = sc.next().toLowerCase();
        
        
        if (index.search(token) == null) {
            System.out.println(0); 
            return;
        }
        
        index.root = index.remove(index.root, token);
    }

    static void print() {
        String output = index.toString();
        if (output.isEmpty()) {
            System.out.println(-1);
        } else {
            System.out.println(output);
        }
    }
}


class Index {

    AVLNode root;
    private AVLNode lastFoundNode;

    public Index() {
        this.root = null;
        this.lastFoundNode = null;
    }

    public AVLNode insert(String key) {
        this.lastFoundNode = null; 
        this.root = insert(this.root, key);
        return this.lastFoundNode; 
    }

    public AVLNode insert(AVLNode root, String key) {
        if (root == null) {
            this.lastFoundNode = new AVLNode(key);
            return this.lastFoundNode;
        }

        int cmp = key.compareTo(root.token);
        
        if (cmp < 0)
            root.left = insert(root.left, key);
        else if (cmp > 0)
            root.right = insert(root.right, key);
        else {
            this.lastFoundNode = root;
            return root; 
        }

        root.height = 1 + Math.max(height(root.left), height(root.right));
        int balanceFactor = getBalance(root);

        if (balanceFactor > 1 && key.compareTo(root.left.token) < 0)
            return rightRotate(root);
        if (balanceFactor < -1 && key.compareTo(root.right.token) > 0)
            return leftRotate(root);
        if (balanceFactor > 1 && key.compareTo(root.left.token) > 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balanceFactor < -1 && key.compareTo(root.right.token) < 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        return root;
    }

    AVLNode minValuNode(AVLNode node) {
        AVLNode cur = node;
        while (cur.left != null) cur = cur.left;
        return cur;
    }

    public AVLNode remove(AVLNode root, String key) {
        if (root == null) return null;

        int cmp = key.compareTo(root.token);

        if (cmp < 0) {
            root.left = remove(root.left, key);
        } else if (cmp > 0) {
            root.right = remove(root.right, key);
        } else {
            if (root.left == null || root.right == null) {
                AVLNode temp = (root.left != null) ? root.left : root.right;
                if (temp == null) root = null; 
                else root = temp; 
            } else {
                AVLNode succ = minValuNode(root.right);
                root.token = succ.token;
                root.list = succ.list;
                root.frequency = succ.frequency;
                root.right = remove(root.right, succ.token);
            }
        }
        if (root == null) return root;

        root.height = Math.max(height(root.left), height(root.right)) + 1;
        int balanceFactor = getBalance(root);

        if (balanceFactor > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);
        if (balanceFactor > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balanceFactor < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);
        if (balanceFactor < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        return root;
    }

    public AVLNode search(String s) {
        if (s == null) return null;
        AVLNode cur = root;
        while (cur != null) {
            int cmp = s.compareTo(cur.token);
            if (cmp == 0) return cur;
            else if (cmp < 0) cur = cur.left;
            else cur = cur.right;
        }
        return null;
    }

    public void traverse(AVLNode node, StringBuilder sb) {
        if (node == null) {
            
            return;
        }

       
        traverse(node.left, sb);
        
        
        traverse(node.right, sb);
        
        
        sb.append(node.token).append(' ');
    }

    private AVLNode leftRotate(AVLNode node) {
        AVLNode rightChild = node.right;
        AVLNode temp = rightChild.left;
        rightChild.left = node;
        node.right = temp;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        rightChild.height = 1 + Math.max(height(rightChild.left), height(rightChild.right));
        return rightChild;
    }

    private AVLNode rightRotate(AVLNode node) {
        AVLNode leftChildAvlNode = node.left;
        AVLNode temp = leftChildAvlNode.right;
        leftChildAvlNode.right = node;
        node.left = temp;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        leftChildAvlNode.height = Math.max(height(leftChildAvlNode.left), height(leftChildAvlNode.right)) + 1;
        return leftChildAvlNode;
    }

    public int height(AVLNode node) {
        if (node == null) return 0;
        return node.height;
    }

    public int getBalance(AVLNode node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        traverse(this.root, sb); 
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}

class AVLNode {
    String token;
    int height;
    int frequency;
    SList list;
    AVLNode left, right;

    public AVLNode(String token) {
        this.token = token;
        this.list = new SList();
        this.height = 1;
        this.frequency = 0;
    }
}

class SList {
    int size;
    Node head;
    Node tail; 

    public SList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public void insert(Node node) {
        if (head == null) {
            head = node;
            tail = node;
            size = 1;
            return;
        }
        tail.next = node;
        tail = node;
        size++;
    }

    public void remove(Node node) {
        if (head == null || node == null) return;
        if (node == head) {
            head = head.next;
            if (head == null) tail = null;
            size--;
            return;
        }
        Node previous = head;
        while (previous.next != null && previous.next != node) {
            previous = previous.next;
        }
        if (previous.next == node) {
            previous.next = node.next;
            if (previous.next == null) tail = previous;
            size--;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        Node current = head;
        while (current != null) {
            s.append(current.fileName).append(' ').append(current.lineNumber);
            current = current.next;
            if (current != null)
                s.append('\n');
        }
        return s.toString();
    }
}

class Node {
    String fileName;
    int lineNumber;
    Node next;

    public Node(String fileName, int lineNumber) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
    }
}
