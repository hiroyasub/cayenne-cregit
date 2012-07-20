begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|Comparator
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|Expression
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|ExpressionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|parser
operator|.
name|ASTDbPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|parser
operator|.
name|ASTObjPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|ConversionUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|XMLEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|XMLSerializable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|ComparatorUtils
import|;
end_import

begin_comment
comment|/**  * Defines object sorting criteria, used either for in-memory sorting of object lists or  * as a specification for building<em>ORDER BY</em> clause of a SelectQuery query. Note  * that in case of in-memory sorting, Ordering can be used with any JavaBeans, not just  * DataObjects.  */
end_comment

begin_class
specifier|public
class|class
name|Ordering
implements|implements
name|Comparator
argument_list|<
name|Object
argument_list|>
implements|,
name|Serializable
implements|,
name|XMLSerializable
block|{
specifier|protected
name|String
name|sortSpecString
decl_stmt|;
specifier|protected
specifier|transient
name|Expression
name|sortSpec
decl_stmt|;
specifier|protected
name|SortOrder
name|sortOrder
decl_stmt|;
specifier|protected
name|boolean
name|pathExceptionSuppressed
init|=
literal|false
decl_stmt|;
specifier|protected
name|boolean
name|nullSortedFirst
init|=
literal|true
decl_stmt|;
comment|/**      * Orders a given list of objects, using a List of Orderings applied according the      * default iteration order of the Orderings list. I.e. each Ordering with lower index      * is more significant than any other Ordering with higher index. List being ordered      * is modified in place.      */
specifier|public
specifier|static
name|void
name|orderList
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|objects
parameter_list|,
name|List
argument_list|<
name|?
extends|extends
name|Ordering
argument_list|>
name|orderings
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|objects
argument_list|,
name|ComparatorUtils
operator|.
name|chainedComparator
argument_list|(
name|orderings
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Ordering
parameter_list|()
block|{
block|}
comment|/**      * @since 3.0      */
specifier|public
name|Ordering
parameter_list|(
name|String
name|sortPathSpec
parameter_list|,
name|SortOrder
name|sortOrder
parameter_list|)
block|{
name|setSortSpecString
argument_list|(
name|sortPathSpec
argument_list|)
expr_stmt|;
name|setSortOrder
argument_list|(
name|sortOrder
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets sortSpec to be an expression represented by string argument.      *       * @since 1.1      */
specifier|public
name|void
name|setSortSpecString
parameter_list|(
name|String
name|sortSpecString
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|this
operator|.
name|sortSpecString
argument_list|,
name|sortSpecString
argument_list|)
condition|)
block|{
name|this
operator|.
name|sortSpecString
operator|=
name|sortSpecString
expr_stmt|;
name|this
operator|.
name|sortSpec
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Sets sort order for whether nulls are at the top or bottom of the resulting list.      * Default is true.      *       * @param nullSortedFirst true sorts nulls to the top of the list, false sorts nulls      *            to the bottom      */
specifier|public
name|void
name|setNullSortedFirst
parameter_list|(
name|boolean
name|nullSortedFirst
parameter_list|)
block|{
name|this
operator|.
name|nullSortedFirst
operator|=
name|nullSortedFirst
expr_stmt|;
block|}
comment|/**      * Get sort order for nulls.      *       * @return true if nulls are sorted to the top of the list, false if sorted to the      *         bottom      */
specifier|public
name|boolean
name|isNullSortedFirst
parameter_list|()
block|{
return|return
name|nullSortedFirst
return|;
block|}
comment|/**      * Sets whether a path with a null in the middle is ignored. For example, a sort from      *<code>painting</code> on<code>artist.name</code> would by default throw an      * exception if the artist was null. If set to true, then this is treated just like a      * null value. Default is false.      *       * @param pathExceptionSuppressed true to suppress exceptions and sort as null      */
specifier|public
name|void
name|setPathExceptionSupressed
parameter_list|(
name|boolean
name|pathExceptionSuppressed
parameter_list|)
block|{
name|this
operator|.
name|pathExceptionSuppressed
operator|=
name|pathExceptionSuppressed
expr_stmt|;
block|}
comment|/**      * Is a path with a null in the middle is ignored.      *       * @return true is exception is suppressed and sorted as null      */
specifier|public
name|boolean
name|isPathExceptionSuppressed
parameter_list|()
block|{
return|return
name|pathExceptionSuppressed
return|;
block|}
comment|/**      * Returns sortSpec string representation.      *       * @since 1.1      */
specifier|public
name|String
name|getSortSpecString
parameter_list|()
block|{
return|return
name|sortSpecString
return|;
block|}
comment|/**      * Sets the sort order for this ordering.      *       * @since 3.0      */
specifier|public
name|void
name|setSortOrder
parameter_list|(
name|SortOrder
name|order
parameter_list|)
block|{
name|this
operator|.
name|sortOrder
operator|=
name|order
expr_stmt|;
block|}
comment|/** Returns true if sorting is done in ascending order. */
specifier|public
name|boolean
name|isAscending
parameter_list|()
block|{
return|return
name|sortOrder
operator|==
literal|null
operator|||
name|sortOrder
operator|==
name|SortOrder
operator|.
name|ASCENDING
operator|||
name|sortOrder
operator|==
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
return|;
block|}
comment|/**      * Returns true if the sorting is done in descending order.      *       * @since 3.0      */
specifier|public
name|boolean
name|isDescending
parameter_list|()
block|{
return|return
operator|!
name|isAscending
argument_list|()
return|;
block|}
comment|/**      * If the sort order is DESCENDING or DESCENDING_INSENSITIVE, sets the sort order to      * ASCENDING or ASCENDING_INSENSITIVE, respectively.      *       * @since 3.0      */
specifier|public
name|void
name|setAscending
parameter_list|()
block|{
if|if
condition|(
name|sortOrder
operator|==
literal|null
operator|||
name|sortOrder
operator|==
name|SortOrder
operator|.
name|DESCENDING
condition|)
name|setSortOrder
argument_list|(
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
if|else if
condition|(
name|sortOrder
operator|==
name|SortOrder
operator|.
name|DESCENDING_INSENSITIVE
condition|)
name|setSortOrder
argument_list|(
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
argument_list|)
expr_stmt|;
block|}
comment|/**      * If the sort order is ASCENDING or ASCENDING_INSENSITIVE, sets the sort order to      * DESCENDING or DESCENDING_INSENSITIVE, respectively.      *       * @since 3.0      */
specifier|public
name|void
name|setDescending
parameter_list|()
block|{
if|if
condition|(
name|sortOrder
operator|==
literal|null
operator|||
name|sortOrder
operator|==
name|SortOrder
operator|.
name|ASCENDING
condition|)
name|setSortOrder
argument_list|(
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
expr_stmt|;
if|else if
condition|(
name|sortOrder
operator|==
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
condition|)
name|setSortOrder
argument_list|(
name|SortOrder
operator|.
name|DESCENDING_INSENSITIVE
argument_list|)
expr_stmt|;
block|}
comment|/** Returns true if the sorting is case insensitive */
specifier|public
name|boolean
name|isCaseInsensitive
parameter_list|()
block|{
return|return
operator|!
name|isCaseSensitive
argument_list|()
return|;
block|}
comment|/**      * Returns true if the sorting is case sensitive.      *       * @since 3.0      */
specifier|public
name|boolean
name|isCaseSensitive
parameter_list|()
block|{
return|return
name|sortOrder
operator|==
literal|null
operator|||
name|sortOrder
operator|==
name|SortOrder
operator|.
name|ASCENDING
operator|||
name|sortOrder
operator|==
name|SortOrder
operator|.
name|DESCENDING
return|;
block|}
comment|/**      * If the sort order is ASCENDING or DESCENDING, sets the sort order to      * ASCENDING_INSENSITIVE or DESCENDING_INSENSITIVE, respectively.      *       * @since 3.0      */
specifier|public
name|void
name|setCaseInsensitive
parameter_list|()
block|{
if|if
condition|(
name|sortOrder
operator|==
literal|null
operator|||
name|sortOrder
operator|==
name|SortOrder
operator|.
name|ASCENDING
condition|)
name|setSortOrder
argument_list|(
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
argument_list|)
expr_stmt|;
if|else if
condition|(
name|sortOrder
operator|==
name|SortOrder
operator|.
name|DESCENDING
condition|)
name|setSortOrder
argument_list|(
name|SortOrder
operator|.
name|DESCENDING_INSENSITIVE
argument_list|)
expr_stmt|;
block|}
comment|/**      * If the sort order is ASCENDING_INSENSITIVE or DESCENDING_INSENSITIVE, sets the sort      * order to ASCENDING or DESCENDING, respectively.      *       * @since 3.0      */
specifier|public
name|void
name|setCaseSensitive
parameter_list|()
block|{
if|if
condition|(
name|sortOrder
operator|==
literal|null
operator|||
name|sortOrder
operator|==
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
condition|)
name|setSortOrder
argument_list|(
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
expr_stmt|;
if|else if
condition|(
name|sortOrder
operator|==
name|SortOrder
operator|.
name|DESCENDING_INSENSITIVE
condition|)
name|setSortOrder
argument_list|(
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the expression defining a ordering Java Bean property.      */
specifier|public
name|Expression
name|getSortSpec
parameter_list|()
block|{
if|if
condition|(
name|sortSpecString
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// compile on demand .. since orderings can only be paths, avoid the overhead of
comment|// Expression.fromString, and parse them manually
if|if
condition|(
name|sortSpec
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|sortSpecString
operator|.
name|startsWith
argument_list|(
name|ASTDbPath
operator|.
name|DB_PREFIX
argument_list|)
condition|)
block|{
name|sortSpec
operator|=
operator|new
name|ASTDbPath
argument_list|(
name|sortSpecString
operator|.
name|substring
argument_list|(
name|ASTDbPath
operator|.
name|DB_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|sortSpecString
operator|.
name|startsWith
argument_list|(
name|ASTObjPath
operator|.
name|OBJ_PREFIX
argument_list|)
condition|)
block|{
name|sortSpec
operator|=
operator|new
name|ASTObjPath
argument_list|(
name|sortSpecString
operator|.
name|substring
argument_list|(
name|ASTObjPath
operator|.
name|OBJ_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sortSpec
operator|=
operator|new
name|ASTObjPath
argument_list|(
name|sortSpecString
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sortSpec
return|;
block|}
comment|/**      * Sets the expression defining a ordering Java Bean property.      */
specifier|public
name|void
name|setSortSpec
parameter_list|(
name|Expression
name|sortSpec
parameter_list|)
block|{
name|this
operator|.
name|sortSpec
operator|=
name|sortSpec
expr_stmt|;
name|this
operator|.
name|sortSpecString
operator|=
operator|(
name|sortSpec
operator|!=
literal|null
operator|)
condition|?
name|sortSpec
operator|.
name|toString
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
comment|/**      * Orders the given list of objects according to the ordering that this object      * specifies. List is modified in-place.      *       * @param objects a List of objects to be sorted      */
specifier|public
name|void
name|orderList
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|objects
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|objects
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
comment|/**      * Comparable interface implementation. Can compare two Java Beans based on the stored      * expression.      */
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|Expression
name|exp
init|=
name|getSortSpec
argument_list|()
decl_stmt|;
name|Object
name|value1
init|=
literal|null
decl_stmt|;
name|Object
name|value2
init|=
literal|null
decl_stmt|;
try|try
block|{
name|value1
operator|=
name|exp
operator|.
name|evaluate
argument_list|(
name|o1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|e
parameter_list|)
block|{
if|if
condition|(
name|pathExceptionSuppressed
operator|&&
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|reflect
operator|.
name|UnresolvablePathException
condition|)
block|{
comment|// do nothing, we expect this
block|}
else|else
block|{
comment|// re-throw
throw|throw
name|e
throw|;
block|}
block|}
try|try
block|{
name|value2
operator|=
name|exp
operator|.
name|evaluate
argument_list|(
name|o2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|e
parameter_list|)
block|{
if|if
condition|(
name|pathExceptionSuppressed
operator|&&
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|reflect
operator|.
name|UnresolvablePathException
condition|)
block|{
comment|// do nothing, we expect this
block|}
else|else
block|{
comment|// rethrow
throw|throw
name|e
throw|;
block|}
block|}
if|if
condition|(
name|value1
operator|==
literal|null
operator|&&
name|value2
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
if|else if
condition|(
name|value1
operator|==
literal|null
condition|)
block|{
return|return
name|nullSortedFirst
condition|?
operator|-
literal|1
else|:
literal|1
return|;
block|}
if|else if
condition|(
name|value2
operator|==
literal|null
condition|)
block|{
return|return
name|nullSortedFirst
condition|?
literal|1
else|:
operator|-
literal|1
return|;
block|}
if|if
condition|(
name|isCaseInsensitive
argument_list|()
condition|)
block|{
comment|// TODO: to upper case should probably be defined as a separate expression
comment|// type
name|value1
operator|=
name|ConversionUtil
operator|.
name|toUpperCase
argument_list|(
name|value1
argument_list|)
expr_stmt|;
name|value2
operator|=
name|ConversionUtil
operator|.
name|toUpperCase
argument_list|(
name|value2
argument_list|)
expr_stmt|;
block|}
name|int
name|compareResult
init|=
name|ConversionUtil
operator|.
name|toComparable
argument_list|(
name|value1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|ConversionUtil
operator|.
name|toComparable
argument_list|(
name|value2
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|(
name|isAscending
argument_list|()
operator|)
condition|?
name|compareResult
else|:
operator|-
name|compareResult
return|;
block|}
comment|/**      * Encodes itself as a query ordering.      *       * @since 1.1      */
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<ordering"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isDescending
argument_list|()
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" descending=\"true\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isCaseInsensitive
argument_list|()
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" ignore-case=\"true\""
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|print
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
if|if
condition|(
name|getSortSpec
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getSortSpec
argument_list|()
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|println
argument_list|(
literal|"</ordering>"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|XMLEncoder
name|encoder
init|=
operator|new
name|XMLEncoder
argument_list|(
name|pw
argument_list|)
decl_stmt|;
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
name|pw
operator|.
name|close
argument_list|()
expr_stmt|;
name|buffer
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns sort order for this ordering      * @since 3.1      */
specifier|public
name|SortOrder
name|getSortOrder
parameter_list|()
block|{
return|return
name|sortOrder
return|;
block|}
block|}
end_class

end_unit

