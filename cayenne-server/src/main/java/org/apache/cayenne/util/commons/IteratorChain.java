begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|commons
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_comment
comment|/**  * An IteratorChain is an Iterator that wraps a number of Iterators.  *<p>  * This class makes multiple iterators look like one to the caller  * When any method from the Iterator interface is called, the IteratorChain  * will delegate to a single underlying Iterator. The IteratorChain will  * invoke the Iterators in sequence until all Iterators are exhausted.  *<p>  * Under many circumstances, linking Iterators together in this manner is  * more efficient (and convenient) than reading out the contents of each  * Iterator into a List and creating a new Iterator.  *<p>  * Calling a method that adds new Iterator<i>after a method in the Iterator  * interface has been called</i> will result in an UnsupportedOperationException.  * Subclasses should<i>take care</i> to not alter the underlying List of Iterators.  *<p>  *  * @since 4.1  *  * @author Morgan Delagrange  * @author Stephen Colebourne  *  * NOTE: this is a simplified version of ItertatorChain class found in commons-collections v3.2.1  * used only by {@link CompositeCollection}.  */
end_comment

begin_class
class|class
name|IteratorChain
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|E
argument_list|>
block|{
comment|/** The chain of iterators */
specifier|protected
specifier|final
name|List
argument_list|<
name|Iterator
argument_list|<
name|E
argument_list|>
argument_list|>
name|iteratorChain
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|/** The index of the current iterator */
specifier|protected
name|int
name|currentIteratorIndex
init|=
literal|0
decl_stmt|;
comment|/** The current iterator */
specifier|protected
name|Iterator
argument_list|<
name|E
argument_list|>
name|currentIterator
init|=
literal|null
decl_stmt|;
comment|/**      * The "last used" Iterator is the Iterator upon which      * next() or hasNext() was most recently called      * used for the remove() operation only      */
specifier|protected
name|Iterator
argument_list|<
name|E
argument_list|>
name|lastUsedIterator
init|=
literal|null
decl_stmt|;
comment|/**      * ComparatorChain is "locked" after the first time      * compare(Object,Object) is called      */
specifier|protected
name|boolean
name|isLocked
init|=
literal|false
decl_stmt|;
comment|//-----------------------------------------------------------------------
comment|/**      * Construct an IteratorChain with no Iterators.      *<p>      * You will normally use {@link #addIterator(Iterator)} to add      * some iterators after using this constructor.      */
specifier|public
name|IteratorChain
parameter_list|()
block|{
block|}
comment|//-----------------------------------------------------------------------
comment|/**      * Add an Iterator to the end of the chain      *      * @param iterator Iterator to add      * @throws IllegalStateException if I've already started iterating      * @throws NullPointerException if the iterator is null      */
specifier|public
name|void
name|addIterator
parameter_list|(
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|)
block|{
name|checkLocked
argument_list|()
expr_stmt|;
name|iteratorChain
operator|.
name|add
argument_list|(
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|iterator
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Number of Iterators in the current IteratorChain.      *      * @return Iterator count      */
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|iteratorChain
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Checks whether the iterator chain is now locked and in use.      */
specifier|private
name|void
name|checkLocked
parameter_list|()
block|{
if|if
condition|(
name|isLocked
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"IteratorChain cannot be changed after the first use of a method from the Iterator interface"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Lock the chain so no more iterators can be added.      * This must be called from all Iterator interface methods.      */
specifier|private
name|void
name|lockChain
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isLocked
condition|)
block|{
name|isLocked
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/**      * Updates the current iterator field to ensure that the current Iterator      * is not exhausted      */
specifier|protected
name|void
name|updateCurrentIterator
parameter_list|()
block|{
if|if
condition|(
name|currentIterator
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|iteratorChain
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|currentIterator
operator|=
name|Collections
operator|.
name|emptyIterator
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|currentIterator
operator|=
name|iteratorChain
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|// set last used iterator here, in case the user calls remove
comment|// before calling hasNext() or next() (although they shouldn't)
name|lastUsedIterator
operator|=
name|currentIterator
expr_stmt|;
block|}
while|while
condition|(
operator|!
name|currentIterator
operator|.
name|hasNext
argument_list|()
operator|&&
name|currentIteratorIndex
operator|<
name|iteratorChain
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|)
block|{
name|currentIteratorIndex
operator|++
expr_stmt|;
name|currentIterator
operator|=
name|iteratorChain
operator|.
name|get
argument_list|(
name|currentIteratorIndex
argument_list|)
expr_stmt|;
block|}
block|}
comment|//-----------------------------------------------------------------------
comment|/**      * Return true if any Iterator in the IteratorChain has a remaining element.      *      * @return true if elements remain      */
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
name|lockChain
argument_list|()
expr_stmt|;
name|updateCurrentIterator
argument_list|()
expr_stmt|;
name|lastUsedIterator
operator|=
name|currentIterator
expr_stmt|;
return|return
name|currentIterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
comment|/**      * Returns the next Object of the current Iterator      *      * @return Object from the current Iterator      * @throws java.util.NoSuchElementException if all the Iterators are exhausted      */
specifier|public
name|E
name|next
parameter_list|()
block|{
name|lockChain
argument_list|()
expr_stmt|;
name|updateCurrentIterator
argument_list|()
expr_stmt|;
name|lastUsedIterator
operator|=
name|currentIterator
expr_stmt|;
return|return
name|currentIterator
operator|.
name|next
argument_list|()
return|;
block|}
comment|/**      * Removes from the underlying collection the last element      * returned by the Iterator.  As with next() and hasNext(),      * this method calls remove() on the underlying Iterator.      * Therefore, this method may throw an      * UnsupportedOperationException if the underlying      * Iterator does not support this method.      *      * @throws UnsupportedOperationException      *   if the remove operator is not supported by the underlying Iterator      * @throws IllegalStateException      *   if the next method has not yet been called, or the remove method has      *   already been called after the last call to the next method.      */
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|lockChain
argument_list|()
expr_stmt|;
if|if
condition|(
name|currentIterator
operator|==
literal|null
condition|)
block|{
name|updateCurrentIterator
argument_list|()
expr_stmt|;
block|}
name|lastUsedIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

