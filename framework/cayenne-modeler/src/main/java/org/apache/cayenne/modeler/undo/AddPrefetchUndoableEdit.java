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
name|javax
operator|.
name|swing
operator|.
name|undo
operator|.
name|AbstractUndoableEdit
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
name|editor
operator|.
name|SelectQueryPrefetchTab
import|;
end_import

begin_class
specifier|public
class|class
name|AddPrefetchUndoableEdit
extends|extends
name|AbstractUndoableEdit
block|{
specifier|private
name|String
name|prefetch
decl_stmt|;
specifier|private
name|SelectQueryPrefetchTab
name|tab
decl_stmt|;
specifier|public
name|AddPrefetchUndoableEdit
parameter_list|(
name|String
name|prefetch
parameter_list|,
name|SelectQueryPrefetchTab
name|tab
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|prefetch
operator|=
name|prefetch
expr_stmt|;
name|this
operator|.
name|tab
operator|=
name|tab
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPresentationName
parameter_list|()
block|{
return|return
literal|"Add Prefetch"
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
name|tab
operator|.
name|addPrefetch
argument_list|(
name|prefetch
argument_list|)
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
name|tab
operator|.
name|removePrefetch
argument_list|(
name|prefetch
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

