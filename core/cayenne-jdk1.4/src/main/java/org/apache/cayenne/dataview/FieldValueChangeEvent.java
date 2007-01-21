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
name|dataview
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventListener
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
name|DataObject
import|;
end_import

begin_class
specifier|public
class|class
name|FieldValueChangeEvent
extends|extends
name|DispatchableEvent
block|{
specifier|private
name|DataObject
name|modifiedObject
decl_stmt|;
specifier|private
name|Object
name|oldValue
decl_stmt|;
specifier|private
name|Object
name|newValue
decl_stmt|;
specifier|public
name|FieldValueChangeEvent
parameter_list|(
name|ObjEntityViewField
name|source
parameter_list|,
name|DataObject
name|modifiedObject
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|this
operator|.
name|modifiedObject
operator|=
name|modifiedObject
expr_stmt|;
name|this
operator|.
name|oldValue
operator|=
name|oldValue
expr_stmt|;
name|this
operator|.
name|newValue
operator|=
name|newValue
expr_stmt|;
block|}
specifier|public
name|void
name|dispatch
parameter_list|(
name|EventListener
name|listener
parameter_list|)
block|{
operator|(
operator|(
name|FieldValueChangeListener
operator|)
name|listener
operator|)
operator|.
name|fieldValueChanged
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ObjEntityViewField
name|getField
parameter_list|()
block|{
return|return
operator|(
name|ObjEntityViewField
operator|)
name|getSource
argument_list|()
return|;
block|}
specifier|public
name|DataObject
name|getModifiedObject
parameter_list|()
block|{
return|return
name|modifiedObject
return|;
block|}
specifier|public
name|Object
name|getNewValue
parameter_list|()
block|{
return|return
name|newValue
return|;
block|}
specifier|public
name|Object
name|getOldValue
parameter_list|()
block|{
return|return
name|oldValue
return|;
block|}
block|}
end_class

end_unit

