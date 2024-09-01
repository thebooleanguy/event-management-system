<script>
	export let isOpen = false;
	export let onClose;
</script>

{#if isOpen}
	<div class="fixed inset-0 flex items-center justify-center z-50">
		<!-- Overlay -->
		<div class="fixed inset-0 bg-black opacity-50" on:click={onClose}></div>

		<!-- Modal Content -->
		<div class="bg-white rounded-lg overflow-hidden shadow-lg p-6 max-w-lg mx-auto z-10">
			<slot></slot>
		</div>
	</div>
{/if}
