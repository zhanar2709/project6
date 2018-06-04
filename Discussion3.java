package ereader;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.swing.tree.TreeNode;

/**
 * The class <code>Discussion3</code> defines a complete implementation of
 * <code>Discussion</code> notes.
 * <p>
 * In this implementation, the original discussion note and all response notes
 * are nodes in a binary tree. Each node has a reference to a (left) child and a
 * (right) sibling. (This is sometimes notated as LCRS.) The child reference is
 * the first node in a linked list of child nodes. The sibling reference is the
 * first node in a linked list of sibling references. Each node also has a
 * reference to its parent node, which is necessary for the node to operate as a
 * Swing <code>TreeNode</code> and can then be used in a <code>JTree</code>. All
 * nodes in the tree are <code>Discussion3</code> notes.
 */
public class Discussion3 extends Discussion implements TreeNode {
	private static final long serialVersionUID = 1L;
	
	private Discussion3 parent;
	private Discussion3 child;
	private Discussion3 sibling;

	/** Sets this discussion note's parent. */
	public void setParent(Discussion3 parent) {
		this.parent = parent;
	}

	/** Returns this discussion note's child note. */
	public Discussion3 getChild() {
		return child;
	}

	/** Sets this discussion note's child note. */
	public void setChild(Discussion3 child) {
		this.child = child;
	}

	/** Returns this discussion note's sibling note. */
	public Discussion3 getSibling() {
		return sibling;
	}

	/** Sets this discussion note's sibling note. */
	public void setSibling(Discussion3 sibling) {
		this.sibling = sibling;
	}

	/** 
	 * Returns the number of responses for this discussion note. The number of responses
	 * is computed as the number of notes in the tree of responses minus one, since the
	 * root of the model is the original discussion note, not a response.
	 */
	public int getResponseCount() {
		int count = countNodes() - 1;
		return count;
	}
	
	/** Returns the number of nodes for the given node and all of its child nodes. */
	private int countNodes() {
		int count = 1;
		
		Discussion3 node = child;
		while (node != null) {
			count += node.countNodes();
			node = node.sibling;
		}

		return count;
	}
	
	/** 
	 * Adds the given response note to this discussion note's list of responses. 
	 * For this implementation, the given response note is added as a child node
	 * to this object.
	 */
	public void addResponse(Discussion3 response) {
		
		// if this note's child is null, set it to the response, return
		if (child == null) {
			child = response;
			return;
		} 
		
		// traverse child nodes until a null sibling is found, then set response
		Discussion3 node = child;
		while (node.sibling != null) {
			node = node.sibling;
		}
		node.sibling = response;
	}
	
	/**
	 * Returns a string description for this discussion note and all response notes.
	 */	
	public String toDeepCopyString() {
		return toDeepCopyString(null);
	}
	
	/**
	 * Returns a string description for this discussion and all response notes.
	 * Responses are number starting at 0. The number for a response is the
	 * given level plus "." plus the index of the response in the response list.
	 * If the given level is null, then the number for a response is simply its index.
	 */
	private String toDeepCopyString(String level) {
		
		// init the return string with the description for this discussion note
		String s = toString();
		
		// add the deep copy string description for each response to this discussion note
		Discussion3 note = child;
		int i = 0;
		while (note != null) {
			String number = (level == null) ? Integer.toString(i) : (level + "." + i);
			note.toDeepCopyString(number);
			s += "\n--- RESPONSE " + number + "\n" + note.toDeepCopyString(number);
			note = note.sibling;
			i++;
		}
		
		return s;
	}

	/* TreeNode interface */

	/** Returns the parent node for this discussion note. */
	public Discussion3 getParent() {
		return parent;
	}

	/** Returns an enumeration of this discussion note's child nodes. */
	public Enumeration<Discussion3> children() {
		return new Enumerator();
	}
	
	/** Returns true if this discussion note allows child nodes. */
	public boolean getAllowsChildren() {
		return true;
	}

	/** Returns the child node at the given index for this discussion note. */
	public TreeNode getChildAt(int index) {
		Discussion3 node = child;
		int i = 0;
		
		while (node != null) {
			if (i == index) {
				return node;
			}
			i++;
			node = node.sibling;
		}
		
		throw new ArrayIndexOutOfBoundsException(index);
	}

	/** Returns the number of child nodes for this discussion note. */
	public int getChildCount() {
		int count = 0;
		Discussion3 node = child;
		while (node != null) {
			count++;
			node = node.sibling;
		}
		return count;
	}

	/** Returns the index for the given node in this discussion note's child nodes. */
	public int getIndex(TreeNode targetNode) {
		int index = 0;
		Discussion3 node = child;
		
		while (node != null) {
			if (node == targetNode) {
				return index;
			}
			index++;
			node = node.sibling;
		}
		
		return -1;
	}

	/**
	 * Returns true if this discussion note has no child nodes, otherwise
	 * returns false.
	 */
	public boolean isLeaf() {
		return (child == null);
	}

	/**
	 * This class defines an object that enumerates this discussion note's
	 * response notes. In terms of the tree structure, this class enumerates all
	 * of the child nodes for the given node.
	 */
	public class Enumerator implements Enumeration<Discussion3> {
		private Discussion3 node;
		
		Enumerator() {
			node = child;
		}
	
		public boolean hasMoreElements() {
			return (node != null);
		}
	
		public Discussion3 nextElement() {
			if (node == null) {
				throw new NoSuchElementException();
			}
			Discussion3 next = node;
			node = node.sibling;
			return next;
		}
	}
}