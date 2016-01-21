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
name|dialog
operator|.
name|pref
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
name|dbimport
operator|.
name|DefaultReverseEngineeringLoader
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
name|dbimport
operator|.
name|DefaultReverseEngineeringWriter
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
name|dbimport
operator|.
name|ReverseEngineering
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
name|dbimport
operator|.
name|ReverseEngineeringLoaderException
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
name|util
operator|.
name|CayenneController
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|InputStream
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
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|XMLFileEditor
extends|extends
name|CayenneController
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOGGER
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|XMLFileEditor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|XMLView
name|XMLview
decl_stmt|;
specifier|public
name|XMLFileEditor
parameter_list|(
name|CayenneController
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|XMLview
operator|=
operator|new
name|XMLView
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ReverseEngineering
name|convertTextIntoReverseEngineering
parameter_list|()
throws|throws
name|ReverseEngineeringLoaderException
block|{
name|String
name|text
init|=
name|XMLview
operator|.
name|getEditorPane
argument_list|()
operator|.
name|getText
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|inputStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|text
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
init|)
block|{
name|ReverseEngineering
name|reverseEngineering
init|=
operator|(
operator|new
name|DefaultReverseEngineeringLoader
argument_list|()
operator|)
operator|.
name|load
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
return|return
name|reverseEngineering
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|addAlertMessage
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|convertReverseEngineeringIntoText
parameter_list|(
name|ReverseEngineering
name|reverseEngineering
parameter_list|)
block|{
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|DefaultReverseEngineeringWriter
name|reverseEngineeringWriter
init|=
operator|new
name|DefaultReverseEngineeringWriter
argument_list|()
decl_stmt|;
name|reverseEngineeringWriter
operator|.
name|write
argument_list|(
name|reverseEngineering
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|XMLview
operator|.
name|getEditorPane
argument_list|()
operator|.
name|setText
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|XMLView
name|getView
parameter_list|()
block|{
return|return
name|XMLview
return|;
block|}
specifier|public
name|void
name|addAlertMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|XMLview
operator|.
name|addAlertMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeAlertMessage
parameter_list|()
block|{
name|XMLview
operator|.
name|removeAlertMessage
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

