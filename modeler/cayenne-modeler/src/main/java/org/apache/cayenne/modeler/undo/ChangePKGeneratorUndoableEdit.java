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
name|modeler
operator|.
name|undo
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
name|DbAttribute
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
name|DbKeyGenerator
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
name|event
operator|.
name|EntityEvent
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
name|modeler
operator|.
name|event
operator|.
name|EntityDisplayEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|CannotRedoException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|CannotUndoException
import|;
end_import

begin_class
specifier|public
class|class
name|ChangePKGeneratorUndoableEdit
extends|extends
name|CayenneUndoableEdit
block|{
specifier|private
name|DbEntity
name|dbEntity
decl_stmt|;
specifier|private
name|PkGeneratorState
name|oldState
decl_stmt|;
specifier|private
name|PkGeneratorState
name|newState
decl_stmt|;
specifier|public
name|ChangePKGeneratorUndoableEdit
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|)
block|{
name|this
operator|.
name|dbEntity
operator|=
name|dbEntity
expr_stmt|;
block|}
specifier|public
name|void
name|captureOldState
parameter_list|()
block|{
name|oldState
operator|=
name|captureState
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|captureNewState
parameter_list|()
block|{
name|newState
operator|=
name|captureState
argument_list|()
expr_stmt|;
block|}
specifier|private
name|PkGeneratorState
name|captureState
parameter_list|()
block|{
return|return
operator|new
name|PkGeneratorState
argument_list|(
name|dbEntity
operator|.
name|getPrimaryKeyGenerator
argument_list|()
argument_list|,
name|findGeneratedAttribute
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|DbAttribute
name|findGeneratedAttribute
parameter_list|()
block|{
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|dbEntity
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
if|if
condition|(
name|attribute
operator|.
name|isGenerated
argument_list|()
condition|)
block|{
return|return
name|attribute
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|redo
parameter_list|()
throws|throws
name|CannotRedoException
block|{
name|newState
operator|.
name|apply
argument_list|()
expr_stmt|;
name|fireEvents
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|undo
parameter_list|()
throws|throws
name|CannotUndoException
block|{
name|oldState
operator|.
name|apply
argument_list|()
expr_stmt|;
name|fireEvents
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|fireEvents
parameter_list|()
block|{
name|controller
operator|.
name|fireDbEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|this
argument_list|,
name|dbEntity
argument_list|)
argument_list|)
expr_stmt|;
name|controller
operator|.
name|fireDbEntityDisplayEvent
argument_list|(
operator|new
name|EntityDisplayEvent
argument_list|(
name|this
argument_list|,
name|dbEntity
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasRealChange
parameter_list|()
block|{
return|return
operator|!
name|oldState
operator|.
name|equals
argument_list|(
name|newState
argument_list|)
return|;
block|}
specifier|private
class|class
name|PkGeneratorState
block|{
specifier|private
name|DbKeyGenerator
name|generator
decl_stmt|;
specifier|private
name|DbAttribute
name|generatedAttribute
decl_stmt|;
specifier|private
name|PkGeneratorState
parameter_list|(
name|DbKeyGenerator
name|generator
parameter_list|,
name|DbAttribute
name|generatedAttribute
parameter_list|)
block|{
name|this
operator|.
name|generator
operator|=
name|generator
expr_stmt|;
name|this
operator|.
name|generatedAttribute
operator|=
name|generatedAttribute
expr_stmt|;
block|}
specifier|private
name|void
name|resetState
parameter_list|()
block|{
name|DbAttribute
name|oldAttribute
init|=
name|findGeneratedAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldAttribute
operator|!=
literal|null
condition|)
block|{
name|oldAttribute
operator|.
name|setGenerated
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|dbEntity
operator|.
name|setPrimaryKeyGenerator
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|apply
parameter_list|()
block|{
name|resetState
argument_list|()
expr_stmt|;
if|if
condition|(
name|generator
operator|!=
literal|null
condition|)
block|{
name|dbEntity
operator|.
name|setPrimaryKeyGenerator
argument_list|(
name|generator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|generatedAttribute
operator|!=
literal|null
condition|)
block|{
name|generatedAttribute
operator|.
name|setGenerated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|PkGeneratorState
name|that
init|=
operator|(
name|PkGeneratorState
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|generator
operator|!=
literal|null
condition|?
operator|!
name|generator
operator|.
name|equals
argument_list|(
name|that
operator|.
name|generator
argument_list|)
else|:
name|that
operator|.
name|generator
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
return|return
name|generatedAttribute
operator|!=
literal|null
condition|?
name|generatedAttribute
operator|.
name|equals
argument_list|(
name|that
operator|.
name|generatedAttribute
argument_list|)
else|:
name|that
operator|.
name|generatedAttribute
operator|==
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|generator
operator|!=
literal|null
condition|?
name|generator
operator|.
name|hashCode
argument_list|()
else|:
literal|0
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|generatedAttribute
operator|!=
literal|null
condition|?
name|generatedAttribute
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
block|}
end_class

end_unit

