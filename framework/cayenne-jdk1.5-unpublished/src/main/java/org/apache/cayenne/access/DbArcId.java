begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
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
name|ObjectId
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
name|map
operator|.
name|DbEntity
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
name|map
operator|.
name|DbRelationship
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
name|EqualsBuilder
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
name|HashCodeBuilder
import|;
end_import

begin_comment
comment|/**  * An id similar to ObjectId that identifies a DbEntity snapshot for implicit  * DbEntities of flattened attributes or relationships. Provides 'equals' and  * 'hashCode' implementations adequate for use as a map key.  *   * @since 3.2  */
end_comment

begin_class
specifier|final
class|class
name|DbArcId
block|{
specifier|private
name|int
name|hashCode
decl_stmt|;
specifier|private
name|ObjectId
name|sourceId
decl_stmt|;
specifier|private
name|DbRelationship
name|incomingArc
decl_stmt|;
specifier|private
name|DbEntity
name|entity
decl_stmt|;
name|DbArcId
parameter_list|(
name|ObjectId
name|sourceId
parameter_list|,
name|DbRelationship
name|incomingArc
parameter_list|)
block|{
name|this
operator|.
name|sourceId
operator|=
name|sourceId
expr_stmt|;
name|this
operator|.
name|incomingArc
operator|=
name|incomingArc
expr_stmt|;
block|}
name|DbEntity
name|getEntity
parameter_list|()
block|{
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
name|entity
operator|=
operator|(
name|DbEntity
operator|)
name|incomingArc
operator|.
name|getTargetEntity
argument_list|()
expr_stmt|;
block|}
return|return
name|entity
return|;
block|}
name|ObjectId
name|getSourceId
parameter_list|()
block|{
return|return
name|sourceId
return|;
block|}
name|DbRelationship
name|getIncominArc
parameter_list|()
block|{
return|return
name|incomingArc
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|hashCode
operator|==
literal|0
condition|)
block|{
name|HashCodeBuilder
name|builder
init|=
operator|new
name|HashCodeBuilder
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|sourceId
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|incomingArc
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|hashCode
operator|=
name|builder
operator|.
name|toHashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|hashCode
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|object
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|DbArcId
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DbArcId
name|id
init|=
operator|(
name|DbArcId
operator|)
name|object
decl_stmt|;
return|return
operator|new
name|EqualsBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|sourceId
argument_list|,
name|id
operator|.
name|sourceId
argument_list|)
operator|.
name|append
argument_list|(
name|incomingArc
operator|.
name|getName
argument_list|()
argument_list|,
name|id
operator|.
name|incomingArc
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isEquals
argument_list|()
return|;
block|}
block|}
end_class

end_unit

