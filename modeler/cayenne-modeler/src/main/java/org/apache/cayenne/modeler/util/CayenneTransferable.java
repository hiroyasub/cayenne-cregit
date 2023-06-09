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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|datatransfer
operator|.
name|DataFlavor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|datatransfer
operator|.
name|Transferable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|datatransfer
operator|.
name|UnsupportedFlavorException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|ConfigurationNodeVisitor
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
name|configuration
operator|.
name|EmptyConfigurationNodeVisitor
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
comment|/**  * CayenneTransferable is a data holder of Cayenne object(s), like  * Entities, Attributes, Relationships etc.  */
end_comment

begin_class
specifier|public
class|class
name|CayenneTransferable
implements|implements
name|Transferable
block|{
comment|/**      * Flavor used for copy-paste between Cayenne Modeler applications      */
specifier|public
specifier|static
specifier|final
name|DataFlavor
name|CAYENNE_FLAVOR
init|=
operator|new
name|DataFlavor
argument_list|(
name|Serializable
operator|.
name|class
argument_list|,
literal|"Cayenne Object"
argument_list|)
decl_stmt|;
comment|/**      * Supported flavors      */
specifier|private
specifier|static
specifier|final
name|DataFlavor
index|[]
name|FLAVORS
init|=
operator|new
name|DataFlavor
index|[]
block|{
name|CAYENNE_FLAVOR
block|,
name|DataFlavor
operator|.
name|stringFlavor
block|}
decl_stmt|;
comment|/**      * Data in the buffer      */
specifier|private
name|Object
name|data
decl_stmt|;
specifier|public
name|CayenneTransferable
parameter_list|(
name|Object
name|data
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
specifier|public
name|Object
name|getTransferData
parameter_list|(
name|DataFlavor
name|flavor
parameter_list|)
throws|throws
name|UnsupportedFlavorException
throws|,
name|IOException
block|{
if|if
condition|(
name|flavor
operator|==
name|CAYENNE_FLAVOR
condition|)
block|{
return|return
name|data
return|;
block|}
else|else
block|{
name|StringWriter
name|out
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLEncoder
name|encoder
init|=
operator|new
name|XMLEncoder
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|out
argument_list|)
argument_list|,
literal|"\t"
argument_list|)
decl_stmt|;
name|ConfigurationNodeVisitor
name|visitor
init|=
operator|new
name|EmptyConfigurationNodeVisitor
argument_list|()
decl_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"utf-8\"?>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|data
operator|instanceof
name|XMLSerializable
condition|)
block|{
operator|(
operator|(
name|XMLSerializable
operator|)
name|data
operator|)
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|data
operator|instanceof
name|List
condition|)
block|{
for|for
control|(
name|Object
name|o
range|:
operator|(
name|List
operator|)
name|data
control|)
block|{
operator|(
operator|(
name|XMLSerializable
operator|)
name|o
operator|)
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
specifier|public
name|DataFlavor
index|[]
name|getTransferDataFlavors
parameter_list|()
block|{
return|return
name|FLAVORS
return|;
block|}
specifier|public
name|boolean
name|isDataFlavorSupported
parameter_list|(
name|DataFlavor
name|flavor
parameter_list|)
block|{
return|return
name|flavor
operator|==
name|CAYENNE_FLAVOR
operator|||
name|flavor
operator|==
name|DataFlavor
operator|.
name|stringFlavor
return|;
block|}
block|}
end_class

end_unit

