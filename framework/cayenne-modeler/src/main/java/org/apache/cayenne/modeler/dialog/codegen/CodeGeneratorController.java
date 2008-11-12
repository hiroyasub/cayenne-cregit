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
name|codegen
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JOptionPane
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
name|gen
operator|.
name|ClassGenerationAction
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
name|DataMap
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
name|dialog
operator|.
name|ErrorDebugDialog
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
name|cayenne
operator|.
name|swing
operator|.
name|BindingBuilder
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
name|collections
operator|.
name|Predicate
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
name|collections
operator|.
name|PredicateUtils
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

begin_comment
comment|/**  * A controller for the class generator dialog.  *   */
end_comment

begin_class
specifier|public
class|class
name|CodeGeneratorController
extends|extends
name|CodeGeneratorControllerBase
block|{
comment|/**      * Logger to print stack traces      */
specifier|private
specifier|static
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ErrorDebugDialog
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|CodeGeneratorDialog
name|view
decl_stmt|;
specifier|protected
name|EntitiesTabController
name|entitySelector
decl_stmt|;
specifier|protected
name|GeneratorTabController
name|generatorSelector
decl_stmt|;
specifier|public
name|CodeGeneratorController
parameter_list|(
name|CayenneController
name|parent
parameter_list|,
name|DataMap
name|dataMap
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
name|this
operator|.
name|entitySelector
operator|=
operator|new
name|EntitiesTabController
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|generatorSelector
operator|=
operator|new
name|GeneratorTabController
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|public
name|void
name|startup
parameter_list|()
block|{
comment|// show dialog even on empty DataMap, as custom generation may still take
comment|// advantage of it
name|view
operator|=
operator|new
name|CodeGeneratorDialog
argument_list|(
name|generatorSelector
operator|.
name|getView
argument_list|()
argument_list|,
name|entitySelector
operator|.
name|getView
argument_list|()
argument_list|)
expr_stmt|;
name|initBindings
argument_list|()
expr_stmt|;
name|view
operator|.
name|pack
argument_list|()
expr_stmt|;
name|view
operator|.
name|setModal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|centerView
argument_list|()
expr_stmt|;
name|makeCloseableOnEscape
argument_list|()
expr_stmt|;
name|view
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initBindings
parameter_list|()
block|{
name|BindingBuilder
name|builder
init|=
operator|new
name|BindingBuilder
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getBindingFactory
argument_list|()
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getCancelButton
argument_list|()
argument_list|,
literal|"cancelAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getGenerateButton
argument_list|()
argument_list|,
literal|"generateAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|this
argument_list|,
literal|"entitySelectedAction()"
argument_list|,
name|SELECTED_PROPERTY
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|generatorSelector
argument_list|,
literal|"generatorSelectedAction()"
argument_list|,
name|GeneratorTabController
operator|.
name|GENERATOR_PROPERTY
argument_list|)
expr_stmt|;
name|generatorSelectedAction
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|generatorSelectedAction
parameter_list|()
block|{
name|GeneratorController
name|controller
init|=
name|generatorSelector
operator|.
name|getGeneratorController
argument_list|()
decl_stmt|;
name|validate
argument_list|(
name|controller
argument_list|)
expr_stmt|;
name|Predicate
name|predicate
init|=
name|controller
operator|!=
literal|null
condition|?
name|controller
operator|.
name|getDefaultEntityFilter
argument_list|()
else|:
name|PredicateUtils
operator|.
name|falsePredicate
argument_list|()
decl_stmt|;
name|updateSelection
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
name|entitySelector
operator|.
name|entitySelectedAction
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|entitySelectedAction
parameter_list|()
block|{
name|int
name|size
init|=
name|getSelectedEntitiesSize
argument_list|()
decl_stmt|;
name|String
name|label
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
name|label
operator|=
literal|"No entities selected"
expr_stmt|;
block|}
if|else if
condition|(
name|size
operator|==
literal|1
condition|)
block|{
name|label
operator|=
literal|"One entity selected"
expr_stmt|;
block|}
else|else
block|{
name|label
operator|=
name|size
operator|+
literal|" entities selected"
expr_stmt|;
block|}
name|view
operator|.
name|getEntityCount
argument_list|()
operator|.
name|setText
argument_list|(
name|label
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|cancelAction
parameter_list|()
block|{
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|generateAction
parameter_list|()
block|{
name|ClassGenerationAction
name|generator
init|=
name|generatorSelector
operator|.
name|getGenerator
argument_list|()
decl_stmt|;
if|if
condition|(
name|generator
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|generator
operator|.
name|execute
argument_list|()
expr_stmt|;
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
literal|"Class generation finished"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logObj
operator|.
name|error
argument_list|(
literal|"Error generating classes"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
operator|.
name|getView
argument_list|()
argument_list|,
literal|"Error generating classes - "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

