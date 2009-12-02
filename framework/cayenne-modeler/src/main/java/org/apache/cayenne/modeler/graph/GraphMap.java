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
name|graph
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|CayenneRuntimeException
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
name|DataDomain
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
name|Application
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
name|ProjectController
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
name|XMLEncoder
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
name|XMLSerializable
import|;
end_import

begin_comment
comment|/**  * Map that stores graph builders<b>for a single domain</b> by their type   * and has additional methods to set currently selected graph and serialize to XML  */
end_comment

begin_class
specifier|public
class|class
name|GraphMap
extends|extends
name|HashMap
argument_list|<
name|GraphType
argument_list|,
name|GraphBuilder
argument_list|>
implements|implements
name|XMLSerializable
block|{
comment|/**      * type that is currently selected      */
name|GraphType
name|selectedType
decl_stmt|;
comment|/**      * Domain      */
name|DataDomain
name|domain
decl_stmt|;
specifier|public
name|GraphMap
parameter_list|(
name|DataDomain
name|domain
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
block|}
comment|/**      * Returns domain      */
specifier|public
name|DataDomain
name|getDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
comment|/**      * Returns type that is currently selected      */
specifier|public
name|GraphType
name|getSelectedType
parameter_list|()
block|{
return|return
name|selectedType
return|;
block|}
comment|/**      * Sets type that is currently selected      */
specifier|public
name|void
name|setSelectedType
parameter_list|(
name|GraphType
name|selectedType
parameter_list|)
block|{
name|this
operator|.
name|selectedType
operator|=
name|selectedType
expr_stmt|;
block|}
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<graphs"
argument_list|)
expr_stmt|;
comment|//        if (selectedType != null) {
comment|//            encoder.print(" selected=\"" + selectedType + "\"");
comment|//        }
name|encoder
operator|.
name|println
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|indent
argument_list|(
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|GraphBuilder
name|builder
range|:
name|values
argument_list|()
control|)
block|{
name|builder
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|indent
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</graphs>"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|GraphBuilder
name|createGraphBuilder
parameter_list|(
name|GraphType
name|type
parameter_list|,
name|boolean
name|doLayout
parameter_list|)
block|{
try|try
block|{
name|GraphBuilder
name|builder
init|=
name|type
operator|.
name|getBuilderClass
argument_list|()
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|builder
operator|.
name|buildGraph
argument_list|(
name|getProjectController
argument_list|()
argument_list|,
name|domain
argument_list|,
name|doLayout
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|type
argument_list|,
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Could not instantiate GraphBuilder"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|//TODO do not use static context
specifier|private
name|ProjectController
name|getProjectController
parameter_list|()
block|{
return|return
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
return|;
block|}
block|}
end_class

end_unit

