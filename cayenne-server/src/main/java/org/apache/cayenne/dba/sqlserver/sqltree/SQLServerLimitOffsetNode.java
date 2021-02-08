begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dba
operator|.
name|sqlserver
operator|.
name|sqltree
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|sqlbuilder
operator|.
name|QuotingAppendable
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|LimitOffsetNode
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|Node
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|SQLServerLimitOffsetNode
extends|extends
name|LimitOffsetNode
block|{
specifier|public
name|SQLServerLimitOffsetNode
parameter_list|(
name|int
name|limit
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
comment|// Per SQLServer documentation: "To retrieve all rows from a certain offset up to the end of the result set,
comment|// you can use some large number for the second parameter."
name|super
argument_list|(
name|limit
operator|==
literal|0
operator|&&
name|offset
operator|>
literal|0
condition|?
name|Integer
operator|.
name|MAX_VALUE
else|:
name|limit
argument_list|,
name|offset
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|QuotingAppendable
name|append
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
if|if
condition|(
name|limit
operator|==
literal|0
operator|&&
name|offset
operator|==
literal|0
condition|)
block|{
return|return
name|buffer
return|;
block|}
return|return
name|buffer
operator|.
name|append
argument_list|(
literal|" OFFSET "
argument_list|)
operator|.
name|append
argument_list|(
name|offset
argument_list|)
operator|.
name|append
argument_list|(
literal|" ROWS FETCH NEXT "
argument_list|)
operator|.
name|append
argument_list|(
name|limit
argument_list|)
operator|.
name|append
argument_list|(
literal|" ROWS ONLY "
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Node
name|copy
parameter_list|()
block|{
return|return
operator|new
name|SQLServerLimitOffsetNode
argument_list|(
name|limit
argument_list|,
name|offset
argument_list|)
return|;
block|}
block|}
end_class

end_unit

