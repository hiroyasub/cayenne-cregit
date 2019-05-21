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
name|modeler
operator|.
name|graph
package|;
end_package

begin_comment
comment|/**  * Types of the graph  */
end_comment

begin_enum
specifier|public
enum|enum
name|GraphType
block|{
comment|/**      * ER-diagram (with DbEntities)      */
name|ER
argument_list|(
literal|"ER Diagram"
argument_list|,
name|DbGraphBuilder
operator|.
name|class
argument_list|)
block|,
comment|/**      * Obj-Diagram (with ObjEntities)      */
name|CLASS
argument_list|(
literal|"Class Diagram"
argument_list|,
name|ObjGraphBuilder
operator|.
name|class
argument_list|)
block|;
comment|/**      * Class to build graphs with      */
name|Class
argument_list|<
name|?
extends|extends
name|GraphBuilder
argument_list|>
name|builderClass
decl_stmt|;
comment|/**      * Readable name      */
name|String
name|name
decl_stmt|;
specifier|private
name|GraphType
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
name|builderClass
parameter_list|)
block|{
name|this
operator|.
name|builderClass
operator|=
name|builderClass
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|GraphBuilder
argument_list|>
name|getBuilderClass
parameter_list|()
block|{
return|return
name|builderClass
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
end_enum

end_unit

