import { ref, type Ref } from 'vue';
import { useEventListener } from '@vueuse/core'
import '@/assets/css.css';

let zIndex = 1;
export default function (containerName: Ref<HTMLElement>) {
  let isMouseDown =false;
  let initX = 0;
  let initY = 0;
  const handleMouseMove = (e: MouseEvent) => {
    if (isMouseDown && containerName.value) {
      containerName.value.classList.add('moving');
      containerName.value!.style.left = e.clientX-initX+ 'px';
      containerName.value!.style.top =e.clientY-initY + 'px';
    }
  };

  const handleMouseUp = () => {
    if (containerName.value) {
        isMouseDown = false;
        containerName.value.classList.remove('moving');
    }
  };

  const handleMouseDown = (e: MouseEvent) => {
    if (containerName.value && (e.target === containerName.value || containerName.value.contains(e.target as Node)) ) {
      isMouseDown = true;
      containerName.value.style.zIndex = (++zIndex).toString();
      const rect = containerName.value.getBoundingClientRect();
      initX = e.clientX - rect.left;
      initY = e.clientY - rect.top;
    }
  };

  const bindEvent = () => {
    if (!containerName.value) return;
    useEventListener(containerName.value, 'mousedown', handleMouseDown);
    useEventListener(window, 'mousemove', handleMouseMove);
    useEventListener(window, 'mouseup', handleMouseUp);
  }
  const unbindEvent = () => {
    if (!containerName.value) return;
    useEventListener(containerName.value, 'mousedown', handleMouseDown, { passive: true, capture: true });
    useEventListener(window, 'mousemove', handleMouseMove, { passive: true, capture: true });
    useEventListener(window, 'mouseup', handleMouseUp, { passive: true, capture: true });
  }

  return { bindEvent,unbindEvent};
}
 
    //  const {bindEvent,unbindEvent}=useMovement(Container as Ref<HTMLElement>)

    // onMounted(() => {
    //     bindEvent()
    // });

    // onBeforeUnmount(() => {
    //     unbindEvent()
    // });