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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
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

begin_comment
comment|/**  * A subclass of {@link DbRelationship} to hold some extra runtime information.  */
end_comment

begin_comment
comment|// TODO: dirty ... can we lookup fkName via joins?
end_comment

begin_class
specifier|public
class|class
name|DbRelationshipDetected
extends|extends
name|DbRelationship
block|{
specifier|private
name|String
name|fkName
decl_stmt|;
specifier|public
name|DbRelationshipDetected
parameter_list|(
name|String
name|uniqueRelName
parameter_list|)
block|{
name|super
argument_list|(
name|uniqueRelName
argument_list|)
expr_stmt|;
block|}
name|DbRelationshipDetected
parameter_list|()
block|{
block|}
comment|/**      * Get the name of the underlying foreign key. Typically FK_NAME from jdbc metadata.      */
specifier|public
name|String
name|getFkName
parameter_list|()
block|{
return|return
name|fkName
return|;
block|}
comment|/**      * Set the name of the underlying foreign key. Typically FK_NAME from jdbc metadata.      */
name|void
name|setFkName
parameter_list|(
name|String
name|fkName
parameter_list|)
block|{
name|this
operator|.
name|fkName
operator|=
name|fkName
expr_stmt|;
block|}
block|}
end_class

end_unit

