package com.test.hello.main;

import java.util.Random;

import com.sun.prism.paint.Stop;

import jdk.nashorn.internal.runtime.FindProperty;

/**
 * 
 * SkipList skipList = new SkipList();
		skipList.init();
		//SkipNode temp = skipList.findNode(2);
		
		skipList.put(3);
		skipList.put(2);
		skipList.put1(1);
		System.out.println(skipList.level);
		System.out.println(skipList.count);
		skipList.printLink();
 * 
 * 跳跃表
 * @author Administrator
 *
 */
class SkipNode {
	
	int val;
	String key;
	SkipNode up;
	SkipNode down;
	SkipNode right;
	SkipNode left;
	
	public SkipNode() {
		up = null;
		down = null;
		right = null;
		left = null;
	}
	
	public SkipNode(int val) {
		// TODO Auto-generated constructor stub
		this.val = val;
		
		up = null;
		down = null;
		right = null;
		left = null;
	}
	
	public SkipNode(String k) {
		// TODO Auto-generated constructor stub
		this.key = k;
		
		up = null;
		down = null;
		right = null;
		left = null;
	}
}


public class SkipList {
	SkipNode top;
	SkipNode tail;
	int level;
	int count;
	
	public void init()
	{
		top = new SkipNode("-*");
		tail = new SkipNode("+*");
		top.right = tail;
		tail.left = top;
		level = 1;
		count = 0;
	}
	
	public SkipNode findNode(int val)
	{
		SkipNode pNode = top;
		
		//空链表只有头尾的话，返回尾节点
		if (pNode.right.key == "+*") {
			return pNode;
		}
		
		while(true) {
			
			while(true) {
				//层找到末尾了
				if (pNode.right.key == "+*") {
					break;
				}
				if (val < pNode.right.val) {
					break;
				}
				pNode = pNode.right;
			}
			

			if (pNode.down != null) {
				pNode = pNode.down;
			} else {
				break;
			}
		}
		
		return pNode;

	}
	
	
	
	public SkipNode put(int val)
	{
		//找到最底层要插入节点的前一个节点
		SkipNode pNode = this.findNode(val);
		
		SkipNode temp = new SkipNode(val);
		
		SkipNode linshi = null;

		pNode.right.left = temp;
		
		temp.right = pNode.right;
		temp.left = pNode;
		pNode.right = temp;
		count++;


		/*Random r = new Random();
		
		while (r.nextDouble() < 0.5) {

			while (pNode.up != null) {
				pNode = pNode.up;
				linshi = new SkipNode(val);
				pNode.right.left = linshi;
				linshi.right = pNode.right;
				linshi.left = pNode;
				pNode.right = linshi;
				count++;
				break;
			}
			
			SkipNode topNew = new SkipNode("-*");
			SkipNode tailNew = new SkipNode("+*");
			top.up = topNew;
			topNew.down = top;
			
			tail.up = tailNew;
			tailNew.down = tail;
			
			top = topNew;
			tail = tailNew;
			
			SkipNode temp2 = new SkipNode(val);

			top.right = temp2;
			temp2.left = top;
			tail.left = temp2;
			temp2.right = tail;

			pNode = temp2;
			level++;
			count++;
			
		}*/
		return null;
	}
	
	
	public SkipNode put1(int val)
	{
		//找到最底层要插入节点的前一个节点
		SkipNode pNode = this.findNode(val);
		
		SkipNode temp = new SkipNode(val);
		
		SkipNode linshi = null;

		pNode.right.left = temp;
		
		temp.right = pNode.right;
		temp.left = pNode;
		pNode.right = temp;
		count++;


		Random r = new Random();
		
		while (r.nextDouble() < 0.5) {

			while (pNode.up != null) {
				pNode = pNode.up;
				linshi = new SkipNode(val);
				pNode.right.left = linshi;
				linshi.right = pNode.right;
				linshi.left = pNode;
				pNode.right = linshi;
				count++;
				break;
			}
			
			SkipNode topNew = new SkipNode("-*");
			SkipNode tailNew = new SkipNode("+*");
			top.up = topNew;
			topNew.down = top;
			
			tail.up = tailNew;
			tailNew.down = tail;
			
			top = topNew;
			tail = tailNew;
			
			SkipNode temp2 = new SkipNode(val);

			top.right = temp2;
			temp2.left = top;
			tail.left = temp2;
			temp2.right = tail;

			pNode = temp2;
			level++;
			count++;
			
		}
		return null;
	}
	
	public void printLink()
	{
		/*for (int i=level ; i>=0 ;i--) {
			
		}*/
		int i = level;
		SkipNode top1 = top;
		
		SkipNode temp = null;
		while (top1 != null) {
			temp = top1;
			while(temp.right.key != "+*"){
				System.out.print(temp.right.val);
				System.out.print(",");
				temp = temp.right;
			}
			top1 = top1.down;
			
			System.out.println("");
		}
	}
	
	
}



