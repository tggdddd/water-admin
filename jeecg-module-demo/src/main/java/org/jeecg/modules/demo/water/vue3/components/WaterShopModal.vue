<template>
  <BasicModal :title="title" :width="800" destroyOnClose v-bind="$attrs" @ok="handleSubmit" @register="registerModal">
    <BasicForm ref="formRef" @register="registerForm"/>
    <!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated @change="handleChangeTabs">
      <a-tab-pane key="waterShopModel" :forceRender="true" tab="规格">
        <JVxeTable
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
  </BasicModal>
</template>

<script lang="ts" setup>
import {computed, reactive, ref, unref} from 'vue';
import {BasicModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form/index';
import {JVxeTable} from '/@/components/jeecg/JVxeTable'
import {useJvxeMethod} from '/@/hooks/system/useJvxeMethods.ts'
import {formSchema, waterShopItemColumns, waterShopModelColumns} from '../WaterShop.data';
import {saveOrUpdate, waterShopItemList, waterShopModelList} from '../WaterShop.api';
// Emits声明
const emit = defineEmits(['register', 'success']);
const isUpdate = ref(true);
const formDisabled = ref(false);
const refKeys = ref(['waterShopModel', 'waterShopItem',]);
const activeKey = ref('waterShopModel');
const waterShopModel = ref();
const waterShopItem = ref();
const tableRefs = {waterShopModel, waterShopItem,};
const waterShopModelTable = reactive({
  loading: false,
  dataSource: [],
  columns: waterShopModelColumns
})
const waterShopItemTable = reactive({
  loading: false,
  dataSource: [],
  columns: waterShopItemColumns
})
//表单配置
const [registerForm, {setProps, resetFields, setFieldsValue, validate}] = useForm({
  //labelWidth: 150,
  schemas: formSchema,
  showActionButtonGroup: false,
  baseColProps: {span: 24}
});
//表单赋值
const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
  //重置表单
  await reset();
  setModalProps({confirmLoading: false, showCancelBtn: data?.showFooter, showOkBtn: data?.showFooter});
  isUpdate.value = !!data?.isUpdate;
  formDisabled.value = !data?.showFooter;
  if (unref(isUpdate)) {
    //表单赋值
    await setFieldsValue({
      ...data.record,
    });
    requestSubTableData(waterShopModelList, {id: data?.record?.id}, waterShopModelTable)
    requestSubTableData(waterShopItemList, {id: data?.record?.id}, waterShopItemTable)
  }
  // 隐藏底部时禁用整个表单
  setProps({disabled: !data?.showFooter})
});
//方法配置
const [handleChangeTabs, handleSubmit, requestSubTableData, formRef] = useJvxeMethod(requestAddOrEdit, classifyIntoFormData, tableRefs, activeKey, refKeys);

//设置标题
const title = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));

async function reset() {
  await resetFields();
  activeKey.value = 'waterShopModel';
  waterShopModelTable.dataSource = [];
  waterShopItemTable.dataSource = [];
}

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
  try {
    setModalProps({confirmLoading: true});
    //提交表单
    await saveOrUpdate(values, isUpdate.value);
    //关闭弹窗
    closeModal();
    //刷新列表
    emit('success');
  } finally {
    setModalProps({confirmLoading: false});
  }
}
</script>

<style lang="less" scoped>
/** 时间和数字输入框样式 */
:deep(.ant-input-number) {
  width: 100%
}

:deep(.ant-calendar-picker) {
  width: 100%
}
</style>