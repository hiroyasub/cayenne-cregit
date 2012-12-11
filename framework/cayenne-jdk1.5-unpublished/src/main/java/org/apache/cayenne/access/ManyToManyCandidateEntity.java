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
name|map
operator|.
name|ObjEntity
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
name|ObjRelationship
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
name|Relationship
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
name|NamedObjectFactory
import|;
end_import

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

begin_comment
comment|/**  * Class represent ObjEntity that may be optimized using flattened relationships  * as many to many table  */
end_comment

begin_class
class|class
name|ManyToManyCandidateEntity
block|{
specifier|private
name|ObjEntity
name|entity
decl_stmt|;
specifier|public
name|ManyToManyCandidateEntity
parameter_list|(
name|ObjEntity
name|entityValue
parameter_list|)
block|{
name|entity
operator|=
name|entityValue
expr_stmt|;
block|}
specifier|public
name|ObjEntity
name|getEntity
parameter_list|()
block|{
return|return
name|entity
return|;
block|}
specifier|private
name|boolean
name|isTargetEntitiesDifferent
parameter_list|()
block|{
return|return
operator|!
name|getTargetEntity1
argument_list|()
operator|.
name|equals
argument_list|(
name|getTargetEntity2
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isRelationshipsHasDependentPK
parameter_list|()
block|{
name|boolean
name|isRelationship1HasDepPK
init|=
name|getDbRelationship1
argument_list|()
operator|.
name|getReverseRelationship
argument_list|()
operator|.
name|isToDependentPK
argument_list|()
decl_stmt|;
name|boolean
name|isRelationship2HasDepPK
init|=
name|getDbRelationship2
argument_list|()
operator|.
name|getReverseRelationship
argument_list|()
operator|.
name|isToDependentPK
argument_list|()
decl_stmt|;
return|return
name|isRelationship1HasDepPK
operator|&&
name|isRelationship2HasDepPK
return|;
block|}
specifier|private
name|ObjRelationship
name|getRelationship1
parameter_list|()
block|{
name|List
argument_list|<
name|Relationship
argument_list|>
name|relationships
init|=
operator|new
name|ArrayList
argument_list|<
name|Relationship
argument_list|>
argument_list|(
name|entity
operator|.
name|getRelationships
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|(
name|ObjRelationship
operator|)
name|relationships
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
specifier|private
name|ObjRelationship
name|getRelationship2
parameter_list|()
block|{
name|List
argument_list|<
name|Relationship
argument_list|>
name|relationships
init|=
operator|new
name|ArrayList
argument_list|<
name|Relationship
argument_list|>
argument_list|(
name|entity
operator|.
name|getRelationships
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|(
name|ObjRelationship
operator|)
name|relationships
operator|.
name|get
argument_list|(
literal|1
argument_list|)
return|;
block|}
specifier|private
name|ObjEntity
name|getTargetEntity1
parameter_list|()
block|{
return|return
operator|(
name|ObjEntity
operator|)
name|getRelationship1
argument_list|()
operator|.
name|getTargetEntity
argument_list|()
return|;
block|}
specifier|private
name|ObjEntity
name|getTargetEntity2
parameter_list|()
block|{
return|return
operator|(
name|ObjEntity
operator|)
name|getRelationship2
argument_list|()
operator|.
name|getTargetEntity
argument_list|()
return|;
block|}
specifier|private
name|DbRelationship
name|getDbRelationship1
parameter_list|()
block|{
return|return
name|getRelationship1
argument_list|()
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
specifier|private
name|DbRelationship
name|getDbRelationship2
parameter_list|()
block|{
return|return
name|getRelationship2
argument_list|()
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**      * Method check - if current entity represent many to many temporary table      * @return true if current entity is represent many to many table; otherwise returns false      */
specifier|public
name|boolean
name|isRepresentManyToManyTable
parameter_list|()
block|{
name|boolean
name|hasTwoRelationships
init|=
name|entity
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|2
decl_stmt|;
name|boolean
name|isNotHaveAttributes
init|=
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
decl_stmt|;
return|return
name|hasTwoRelationships
operator|&&
name|isNotHaveAttributes
operator|&&
name|isRelationshipsHasDependentPK
argument_list|()
operator|&&
name|isTargetEntitiesDifferent
argument_list|()
return|;
block|}
specifier|private
name|void
name|removeRelationshipsFromTargetEntities
parameter_list|()
block|{
name|getTargetEntity1
argument_list|()
operator|.
name|removeRelationship
argument_list|(
name|getRelationship1
argument_list|()
operator|.
name|getReverseRelationship
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|getTargetEntity2
argument_list|()
operator|.
name|removeRelationship
argument_list|(
name|getRelationship2
argument_list|()
operator|.
name|getReverseRelationship
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addFlattenedRelationship
parameter_list|(
name|ObjEntity
name|srcEntity
parameter_list|,
name|ObjEntity
name|dstEntity
parameter_list|,
name|DbRelationship
modifier|...
name|relationshipPath
parameter_list|)
block|{
name|ObjRelationship
name|newRelationship
init|=
operator|(
name|ObjRelationship
operator|)
name|NamedObjectFactory
operator|.
name|createRelationship
argument_list|(
name|srcEntity
argument_list|,
name|dstEntity
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|newRelationship
operator|.
name|setSourceEntity
argument_list|(
name|srcEntity
argument_list|)
expr_stmt|;
name|newRelationship
operator|.
name|setTargetEntity
argument_list|(
name|dstEntity
argument_list|)
expr_stmt|;
for|for
control|(
name|DbRelationship
name|curRelationship
range|:
name|relationshipPath
control|)
block|{
name|newRelationship
operator|.
name|addDbRelationship
argument_list|(
name|curRelationship
argument_list|)
expr_stmt|;
block|}
name|srcEntity
operator|.
name|addRelationship
argument_list|(
name|newRelationship
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method make direct relationships between 2 entities and remove relationships to      * many to many entity      */
specifier|public
name|void
name|optimizeRelationships
parameter_list|()
block|{
name|removeRelationshipsFromTargetEntities
argument_list|()
expr_stmt|;
name|DbRelationship
name|dbRelationship1
init|=
name|getRelationship1
argument_list|()
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|DbRelationship
name|dbRelationship2
init|=
name|getRelationship2
argument_list|()
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|addFlattenedRelationship
argument_list|(
name|getTargetEntity1
argument_list|()
argument_list|,
name|getTargetEntity2
argument_list|()
argument_list|,
name|dbRelationship1
operator|.
name|getReverseRelationship
argument_list|()
argument_list|,
name|dbRelationship2
argument_list|)
expr_stmt|;
name|addFlattenedRelationship
argument_list|(
name|getTargetEntity2
argument_list|()
argument_list|,
name|getTargetEntity1
argument_list|()
argument_list|,
name|dbRelationship2
operator|.
name|getReverseRelationship
argument_list|()
argument_list|,
name|dbRelationship1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

