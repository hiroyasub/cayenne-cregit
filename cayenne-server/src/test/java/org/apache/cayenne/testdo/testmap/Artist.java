begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|testdo
operator|.
name|testmap
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
name|testdo
operator|.
name|testmap
operator|.
name|annotations
operator|.
name|Tag1
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
name|testdo
operator|.
name|testmap
operator|.
name|auto
operator|.
name|_Artist
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
name|unit
operator|.
name|util
operator|.
name|ValidationDelegate
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_class
annotation|@
name|Tag1
specifier|public
class|class
name|Artist
extends|extends
name|_Artist
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|protected
specifier|transient
name|ValidationDelegate
name|validationDelegate
decl_stmt|;
specifier|protected
name|boolean
name|validateForSaveCalled
decl_stmt|;
specifier|protected
name|boolean
name|postAdded
decl_stmt|;
specifier|protected
name|boolean
name|prePersisted
decl_stmt|;
specifier|protected
name|boolean
name|preRemoved
decl_stmt|;
specifier|protected
name|boolean
name|preUpdated
decl_stmt|;
specifier|protected
name|boolean
name|postUpdated
decl_stmt|;
specifier|protected
name|boolean
name|postRemoved
decl_stmt|;
specifier|protected
name|boolean
name|postPersisted
decl_stmt|;
specifier|protected
name|int
name|postLoaded
decl_stmt|;
specifier|protected
specifier|transient
name|int
name|propertyWrittenDirectly
decl_stmt|;
specifier|protected
name|String
name|someOtherProperty
decl_stmt|;
specifier|protected
name|Object
name|someOtherObjectProperty
decl_stmt|;
specifier|public
name|boolean
name|isValidateForSaveCalled
parameter_list|()
block|{
return|return
name|validateForSaveCalled
return|;
block|}
specifier|public
name|void
name|resetValidationFlags
parameter_list|()
block|{
name|validateForSaveCalled
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|void
name|setValidationDelegate
parameter_list|(
name|ValidationDelegate
name|validationDelegate
parameter_list|)
block|{
name|this
operator|.
name|validationDelegate
operator|=
name|validationDelegate
expr_stmt|;
block|}
specifier|public
name|void
name|resetCallbackFlags
parameter_list|()
block|{
name|postAdded
operator|=
literal|false
expr_stmt|;
name|prePersisted
operator|=
literal|false
expr_stmt|;
name|preRemoved
operator|=
literal|false
expr_stmt|;
name|preUpdated
operator|=
literal|false
expr_stmt|;
name|postUpdated
operator|=
literal|false
expr_stmt|;
name|postRemoved
operator|=
literal|false
expr_stmt|;
name|postPersisted
operator|=
literal|false
expr_stmt|;
name|postLoaded
operator|=
literal|0
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|validateForSave
parameter_list|(
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|validateForSaveCalled
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|validationDelegate
operator|!=
literal|null
condition|)
block|{
name|validationDelegate
operator|.
name|validateForSave
argument_list|(
name|this
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|validateForSave
argument_list|(
name|validationResult
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|postAddCallback
parameter_list|()
block|{
name|postAdded
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|prePersistCallback
parameter_list|()
block|{
name|prePersisted
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|preRemoveCallback
parameter_list|()
block|{
name|preRemoved
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|preUpdateCallback
parameter_list|()
block|{
name|preUpdated
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|postUpdateCallback
parameter_list|()
block|{
name|postUpdated
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|postPersistCallback
parameter_list|()
block|{
name|postPersisted
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|postRemoveCallback
parameter_list|()
block|{
name|postRemoved
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|postLoadCallback
parameter_list|()
block|{
name|postLoaded
operator|++
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPostAdded
parameter_list|()
block|{
return|return
name|postAdded
return|;
block|}
specifier|public
name|boolean
name|isPrePersisted
parameter_list|()
block|{
return|return
name|prePersisted
return|;
block|}
specifier|public
name|boolean
name|isPreRemoved
parameter_list|()
block|{
return|return
name|preRemoved
return|;
block|}
specifier|public
name|boolean
name|isPreUpdated
parameter_list|()
block|{
return|return
name|preUpdated
return|;
block|}
specifier|public
name|boolean
name|isPostUpdated
parameter_list|()
block|{
return|return
name|postUpdated
return|;
block|}
specifier|public
name|boolean
name|isPostRemoved
parameter_list|()
block|{
return|return
name|postRemoved
return|;
block|}
specifier|public
name|boolean
name|isPostPersisted
parameter_list|()
block|{
return|return
name|postPersisted
return|;
block|}
specifier|public
name|int
name|getPostLoaded
parameter_list|()
block|{
return|return
name|postLoaded
return|;
block|}
specifier|public
name|String
name|getSomeOtherProperty
parameter_list|()
block|{
return|return
name|someOtherProperty
return|;
block|}
specifier|public
name|void
name|setSomeOtherProperty
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|someOtherProperty
operator|=
name|string
expr_stmt|;
block|}
specifier|public
name|Object
name|getSomeOtherObjectProperty
parameter_list|()
block|{
return|return
name|someOtherObjectProperty
return|;
block|}
specifier|public
name|void
name|setSomeOtherObjectProperty
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|someOtherObjectProperty
operator|=
name|object
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writePropertyDirectly
parameter_list|(
name|String
name|propName
parameter_list|,
name|Object
name|val
parameter_list|)
block|{
name|propertyWrittenDirectly
operator|++
expr_stmt|;
name|super
operator|.
name|writePropertyDirectly
argument_list|(
name|propName
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getPropertyWrittenDirectly
parameter_list|()
block|{
return|return
name|propertyWrittenDirectly
return|;
block|}
block|}
end_class

end_unit

