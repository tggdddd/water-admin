<template>
  <div>
    <BasicForm ref="formRef" @register="registerForm"/>
    <!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated @change="handleChangeTabs">
      <a-tab-pane key="waterShopModel" :forceRender="true" tab="规格">
        <JVxeTable
            v-if="waterShopModelTable.show"
            ref="waterShopModel"
            :columns="waterShopModelTable.columns"
            :dataSource="waterShopModelTable.dataSource"
            :disabled="formDisabled"
            :height="340"
            :loading="waterShopModelTable.loading"
            :rowNumber="true"
            :rowSelection="true"
            :toolbar="true"
            keep-source
            resizable
        />
      </a-tab-pane>
      <a-tab-pane key="waterShopItem" :forceRender="true" tab="售卖商品">
        <JVxeTable
            v-if="waterShopItemTable.show"
            ref="waterShopItem"
            :columns="waterShopItemTable.columns"
            :dataSource="waterShopItemTable.dataSource"
            :disabled="formDisabled"
            :height="340"
            :loading="waterShopItemTable.loading"
            :rowNumber="true"
            :rowSelection="true"
            :toolbar="true"
            keep-source
            resizable
        />
      </a-tab-pane>
    </a-tabs>

    <div v-if="!formDisabled" style="width: 100%;text-align: center">
      <a-button pre-icon="ant-design:check" type="primary" @click="handleSubmit">提 交</a-button>
    </div>
  </div>
</template>

<script lang="ts">

import {BasicForm, useForm} from '/@/components/Form/index';
import {computed, defineComponent, reactive, ref} from 'vue';
import {defHttp} from '/@/utils/http/axios';
import {propTypes} from '/@/utils/propTypes';
import {useJvxeMethod} from '/@/hooks/system/useJvxeMethods';
import {getBpmFormSchema, waterShopItemColumns, waterShopModelColumns} from '../WaterShop.data';
import {saveOrUpdate, waterShopItemList, waterShopModelList} from '../WaterShop.api';

export default defineComponent({
  name: "WaterShopForm",
  components: {
    BasicForm,
  },
  props: {
    formData: propTypes.object.def({}),
    formBpm: propTypes.bool.def(true),
  },
  setup(props) {
    const [registerForm, {setFieldsValue, setProps}] = useForm({
      labelWidth: 150,
      schemas: getBpmFormSchema(props.formData),
      showActionButtonGroup: false,
      baseColProps: {span: 24}
    });

    const formDisabled = computed(() => {
      if (props.formData.disabled === false) {
        return false;
      }
      return true;
    });

    const refKeys = ref(['waterShopModel', 'waterShopItem',]);
    const activeKey = ref('waterShopModel');
    const waterShopModel = ref();
    const waterShopItem = ref();
    const tableRefs = {waterShopModel, waterShopItem,};
    const waterShopModelTable = reactive({
      loading: false,
      dataSource: [],
      columns: waterShopModelColumns,
      show: false
    })
    const waterShopItemTable = reactive({
      loading: false,
      dataSource: [],
      columns: waterShopItemColumns,
      show: false
    })

    const [handleChangeTabs, handleSubmit, requestSubTableData, formRef] = useJvxeMethod(requestAddOrEdit, classifyIntoFormData, tableRefs, activeKey, refKeys, validateSubForm);

    function classifyIntoFormData(allValues) {
      let main = Object.assign({}, allValues.formValue)
      return {
        ...main, // 展开
        waterShopModelList: allValues.tablesValue[0].tableData,
        waterShopItemList: allValues.tablesValue[1].tableData,
      }
    }

    //表单提交事件
    async function requestAddOrEdit(values) {
      await saveOrUpdate(values, true);
    }

    const queryByIdUrl = '/water/waterShop/queryById';

    async function initFormData() {
      let params = {id: props.formData.dataId};
      const data = await defHttp.get({url: queryByIdUrl, params});
      //设置表单的值
      await setFieldsValue({...data});
      requestSubTableData(waterShopModelList, {id: data.id}, waterShopModelTable, () => {
        waterShopModelTable.show = true;
      });
      requestSubTableData(waterShopItemList, {id: data.id}, waterShopItemTable, () => {
        waterShopItemTable.show = true;
      });
      //默认是禁用
      await setProps({disabled: formDisabled.value})
    }

    initFormData();

    return {
      registerForm,
      formDisabled,
      formRef,
      handleSubmit,
      activeKey,
      handleChangeTabs,
      waterShopModel,
      waterShopItem,
      waterShopModelTable,
      waterShopItemTable,
    }
  }
});
</script>