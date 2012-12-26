begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
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
name|ExpressionFactory
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
name|query
operator|.
name|Ordering
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
name|query
operator|.
name|PrefetchTreeNode
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
name|query
operator|.
name|SortOrder
import|;
end_import

begin_comment
comment|/**  *<p>  * A property in a DataObject.  *</p>  *   *<p>  * Used to construct Expressions quickly and with type-safety, and to construct  * Orderings  *</p>  *   *<p>  * Instances of this class are immutable  *</p>  *   * @param<E>  *            The type this property returns.  * @since 3.2  */
end_comment

begin_class
specifier|public
class|class
name|Property
parameter_list|<
name|E
parameter_list|>
block|{
comment|/**      * Name of the property in the object      */
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
comment|/**      * Constructs a new property with the given name.      */
specifier|public
name|Property
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * @return Name of the property in the object.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * @return Constructs a property path by appending the argument to the      *         existing property separated by a dot      */
specifier|public
name|Property
argument_list|<
name|Object
argument_list|>
name|dot
parameter_list|(
name|String
name|property
parameter_list|)
block|{
return|return
operator|new
name|Property
argument_list|<
name|Object
argument_list|>
argument_list|(
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|property
argument_list|)
return|;
block|}
comment|/**      * @return Constructs a property path by appending the argument to the      *         existing property separated by a dot      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Property
argument_list|<
name|T
argument_list|>
name|dot
parameter_list|(
name|Property
argument_list|<
name|T
argument_list|>
name|property
parameter_list|)
block|{
return|return
operator|new
name|Property
argument_list|<
name|T
argument_list|>
argument_list|(
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|property
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return An expression representing null.      */
specifier|public
name|Expression
name|isNull
parameter_list|()
block|{
return|return
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * @return An expression representing a non-null value.      */
specifier|public
name|Expression
name|isNotNull
parameter_list|()
block|{
return|return
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
operator|.
name|notExp
argument_list|()
return|;
block|}
comment|/**      * @return An expression representing equality to TRUE.      */
specifier|public
name|Expression
name|isTrue
parameter_list|()
block|{
return|return
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|getName
argument_list|()
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
return|;
block|}
comment|/**      * @return An expression representing equality to FALSE.      */
specifier|public
name|Expression
name|isFalse
parameter_list|()
block|{
return|return
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|getName
argument_list|()
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
return|;
block|}
comment|/**      * @return An expression representing equality to a value.      */
specifier|public
name|Expression
name|eq
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return An expression representing inequality to a value.      */
specifier|public
name|Expression
name|ne
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|noMatchExp
argument_list|(
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return An expression for a Database "Like" query.      */
specifier|public
name|Expression
name|like
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return An expression for a case insensitive "Like" query.      */
specifier|public
name|Expression
name|likeInsensitive
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|likeIgnoreCaseExp
argument_list|(
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return An expression checking for objects between a lower and upper      *         bound inclusive      *       * @param lower      *            The lower bound.      * @param upper      *            The upper bound.      */
specifier|public
name|Expression
name|between
parameter_list|(
name|E
name|lower
parameter_list|,
name|E
name|upper
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|betweenExp
argument_list|(
name|getName
argument_list|()
argument_list|,
name|lower
argument_list|,
name|upper
argument_list|)
return|;
block|}
comment|/**      * @return An expression for finding objects with values in the given set.      */
specifier|public
name|Expression
name|in
parameter_list|(
name|E
modifier|...
name|values
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
name|getName
argument_list|()
argument_list|,
name|values
argument_list|)
return|;
block|}
comment|/**      * @return A greater than Expression.      */
specifier|public
name|Expression
name|gt
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|greaterExp
argument_list|(
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return A greater than or equal to Expression.      */
specifier|public
name|Expression
name|gte
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return A less than Expression.      */
specifier|public
name|Expression
name|lt
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|lessExp
argument_list|(
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return A less than or equal to Expression.      */
specifier|public
name|Expression
name|lte
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|lessOrEqualExp
argument_list|(
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return Ascending sort orderings on this property.      */
specifier|public
name|Ordering
name|asc
parameter_list|()
block|{
return|return
operator|new
name|Ordering
argument_list|(
name|getName
argument_list|()
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
return|;
block|}
comment|/**      * @return Ascending sort orderings on this property.      */
specifier|public
name|List
argument_list|<
name|Ordering
argument_list|>
name|ascs
parameter_list|()
block|{
name|List
argument_list|<
name|Ordering
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|Ordering
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|asc
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * @return Ascending case insensitive sort orderings on this property.      */
specifier|public
name|Ordering
name|ascInsensitive
parameter_list|()
block|{
return|return
operator|new
name|Ordering
argument_list|(
name|getName
argument_list|()
argument_list|,
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
argument_list|)
return|;
block|}
comment|/**      * @return Ascending case insensitive sort orderings on this property.      */
specifier|public
name|List
argument_list|<
name|Ordering
argument_list|>
name|ascInsensitives
parameter_list|()
block|{
name|List
argument_list|<
name|Ordering
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|Ordering
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|ascInsensitive
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * @return Descending sort orderings on this property.      */
specifier|public
name|Ordering
name|desc
parameter_list|()
block|{
return|return
operator|new
name|Ordering
argument_list|(
name|getName
argument_list|()
argument_list|,
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
return|;
block|}
comment|/**      * @return Descending sort orderings on this property.      */
specifier|public
name|List
argument_list|<
name|Ordering
argument_list|>
name|descs
parameter_list|()
block|{
name|List
argument_list|<
name|Ordering
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|Ordering
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|desc
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * @return Descending case insensitive sort orderings on this property.      */
specifier|public
name|Ordering
name|descInsensitive
parameter_list|()
block|{
return|return
operator|new
name|Ordering
argument_list|(
name|getName
argument_list|()
argument_list|,
name|SortOrder
operator|.
name|DESCENDING_INSENSITIVE
argument_list|)
return|;
block|}
comment|/**      * @return Descending case insensitive sort orderings on this property.      */
specifier|public
name|List
argument_list|<
name|Ordering
argument_list|>
name|descInsensitives
parameter_list|()
block|{
name|List
argument_list|<
name|Ordering
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|Ordering
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|descInsensitive
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|PrefetchTreeNode
name|joint
parameter_list|()
block|{
name|PrefetchTreeNode
name|node
init|=
name|prefetch
argument_list|()
decl_stmt|;
name|node
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
specifier|public
name|PrefetchTreeNode
name|disjoint
parameter_list|()
block|{
name|PrefetchTreeNode
name|node
init|=
name|prefetch
argument_list|()
decl_stmt|;
name|node
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
specifier|public
name|PrefetchTreeNode
name|disjointById
parameter_list|()
block|{
name|PrefetchTreeNode
name|node
init|=
name|prefetch
argument_list|()
decl_stmt|;
name|node
operator|.
name|setSemantics
argument_list|(
name|PrefetchTreeNode
operator|.
name|DISJOINT_BY_ID_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
name|PrefetchTreeNode
name|prefetch
parameter_list|()
block|{
comment|// TODO: not very efficient - we are creating a prefetch that
comment|// SelectQuery would throw away and recreate...
name|PrefetchTreeNode
name|root
init|=
operator|new
name|PrefetchTreeNode
argument_list|()
decl_stmt|;
name|PrefetchTreeNode
name|node
init|=
name|root
operator|.
name|addPath
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|node
operator|.
name|setPhantom
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
block|}
end_class

end_unit

