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
name|jpa
operator|.
name|conf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|AnnotatedElement
import|;
end_import

begin_comment
comment|/**  * An abstract JPA annotation processor.  *   * @author Andrus Adamchik  */
end_comment

begin_interface
specifier|public
interface|interface
name|AnnotationProcessor
block|{
comment|/**      * Invoked in the beginning of the annotation tree traversal, which is done in a      * depth-first manner.      */
name|void
name|onStartElement
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|,
name|AnnotationProcessorStack
name|context
parameter_list|)
function_decl|;
comment|/**      * Invoked at the end of the annotation tree traversal, which is done in a depth-first      * manner.      */
name|void
name|onFinishElement
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|,
name|AnnotationProcessorStack
name|context
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

